package projekt1;


import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.SetIterable;
import org.eclipse.collections.impl.multimap.list.FastListMultimap;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.security.Key;
import java.util.*;
import java.util.Calendar;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Math.abs;
import static java.lang.String.valueOf;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Pesel{
    private static String Data;
    private static String DataToSave;
    public static void main(String[] args) throws SchedulerException, InterruptedException {
        Map<String, PeopleData> people = new HashMap<>();
        Timer t1 = new Timer();
        long secs = ((new Date().getTime())/1000)%30;
        t1.schedule(new AutoSave(), (30-secs)*1000,30000);
        t1.schedule(new TimeTo(), 0, 60000);
        while(true){
            //podanie nazwy miasta
            Thread.sleep(200);
            DataToSave = Data;
            System.out.println("Enter city name: ");
            Scanner scanner = new Scanner(System.in);
            String cityName = scanner.nextLine();
            //podanie
            // imienia, nazwiska i peselu z okreslona skladnia
            System.out.println("Enter your name, surname, PESEL with spaces between: ");
            String data = scanner.nextLine();
            try {
                Data = "";
                //podzielenie input'u po spacjach
                String[] splited = data.split("\\s+");
                String name = splited[0] + " " + splited[1];
                String PESEL = splited[2];
                while (PESEL.length() != 11) {
                    System.err.println("Enter valid length of PESEL: ");
                    PESEL = scanner.nextLine();
                }
                //sprawdzenie czy Pesel jest odpowiedniego typu
                long checkifNumeric = parseLong(PESEL);
                Boolean peselChecker = PESELChecker(PESEL);
                while (!peselChecker) {
                    System.err.println("\nInvalid PESEL format, please type in valid format again.\n");
                    PESEL = scanner.nextLine();
                    peselChecker = PESELChecker(PESEL);
                }
                PeopleData peopleData = new PeopleData(PESEL, name, cityName);
                people.put(PESEL, peopleData);
                List<PeopleData> list = new ArrayList<PeopleData>(people.values());

                //first element of list
                Collections.sort(list, new Sortbycity());
                PeopleData first = list.get(0);
                Data += first.getCity();
                for(PeopleData model : list){
                    if(model.getCity().equals(first.getCity())) {
                        Data += "\n " + model.getFullname() + "\n   PESEL: " + model.getPESEL() + "\n";
                    }
                    else{
                        first = model;
                        Data += first.getCity();
                        Data += "\n " + first.getFullname() + "\n   PESEL: " + first.getPESEL() + "\n";
                    }
                }

            }
            catch (Exception e) {
                System.err.println("Invalid data format!");
            }
        }

    }
    public static Boolean PESELChecker(String PESEL){
        try {
            int check = Character.getNumericValue(PESEL.charAt(0)) * 9 + Character.getNumericValue(PESEL.charAt(1)) * 7 + Character.getNumericValue(PESEL.charAt(2)) * 3 + Character.getNumericValue(PESEL.charAt(3)) + Character.getNumericValue(PESEL.charAt(4)) * 9 + Character.getNumericValue(PESEL.charAt(5)) * 7 + Character.getNumericValue(PESEL.charAt(6)) * 3 + Character.getNumericValue(PESEL.charAt(7)) + Character.getNumericValue(PESEL.charAt(8)) * 9 + Character.getNumericValue(PESEL.charAt(9)) * 7;
            if ((check % 10 == 0 || Character.getNumericValue(PESEL.charAt(10)) == check % 10) && PESEL.length() == 11) {
                return true;
            }
            return false;
        }
        catch(Exception e){
            return false;
        }
    }
    static class AutoSave extends TimerTask {
        public void run() {
            try {
                PrintWriter out = new PrintWriter(new FileOutputStream("odp.txt", false));
                out.print(DataToSave);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    static class TimeTo extends  TimerTask {
        public void run() {
            Timetable br = new Timetable();
            Calendar cal = Calendar.getInstance();
            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
            int currentMin = cal.get(Calendar.MINUTE);
            int currentDay = cal.get(Calendar.DAY_OF_WEEK);
            int minutesInDay = currentHour * 60 + currentMin;
            for (int x = 0; x < br.classesInMinutes.length; x++) {
                if (minutesInDay <= (br.breaksInMinutes[x]) && (currentDay < 6 && currentDay > 1)) {
                    if (br.breaksInMinutes[x] < br.classesInMinutes[x] || br.classesInMinutes[x] < minutesInDay) {
                        System.out.println("Time to break is: " + (abs(br.breaksInMinutes[x] - minutesInDay)) / 60 + " hours and " + (abs(br.breaksInMinutes[x] - minutesInDay)) % 60 + " minutes.");
                        break;
                    } else {
                        System.out.println("Time to break is: " + (abs(br.classesInMinutes[x] - minutesInDay)) / 60 + " hours and " + (abs(br.classesInMinutes[x] - minutesInDay)) % 60 + " minutes.");
                        break;
                    }
                } else if (currentDay == 6 || currentDay == 7) {
                    if (minutesInDay > br.breaksInMinutes[x]) {
                        System.out.println("Time to classes is: " + (abs(br.classesInMinutes[0] + (1440 - minutesInDay)) / 60 + (24 * (8 - currentDay))) + " hours and " + (abs(br.classesInMinutes[0] + (1440 - minutesInDay)) / 60 + (24 * (8 - currentDay))) % 60 + " minutes.");
                        break;
                    } else {
                        System.out.println("Time to classes is: " + (abs(br.classesInMinutes[0] + minutesInDay) / 60 + (24 * (9 - currentDay))) + " hours and " + (abs(br.classesInMinutes[0] + minutesInDay) / 60 + (24 * (9 - currentDay))) % 60 + " minutes.");
                        break;
                    }
                } else if (minutesInDay > br.breaksInMinutes[br.breaksInMinutes.length - 1]) {
                    System.out.println("Time to classes is: " + abs(br.classesInMinutes[0] + (1440 - minutesInDay)) + " hours and " + abs(br.classesInMinutes[0] + (1440 - minutesInDay))%60 + " minutes.");
                }
            }
        }
    }
    public static Integer TimeTest(int testTime){
        Timetable br = new Timetable();
        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);
        for (int x = 0; x < br.classesInMinutes.length; x++){
            if(testTime <= (br.breaksInMinutes[x]) && (currentDay < 6 && currentDay > 1)) {
                if (br.breaksInMinutes[x] < br.classesInMinutes[x] || br.classesInMinutes[x] < testTime) {
                    return (abs(br.breaksInMinutes[x] - testTime));
                }
                else {
                    return (abs(br.classesInMinutes[x] - testTime));
                }
            }
            else if(currentDay == 6 || currentDay == 7){
                if(testTime > br.breaksInMinutes[x]) {
                    return (abs(br.classesInMinutes[0] + (1440-testTime)) + ((24 * (8 - currentDay)) * 60));
                }
                else {
                    return (abs(br.classesInMinutes[0] - testTime) + (24 * (9 - currentDay)*60));
                }
            }
            else if(testTime > br.breaksInMinutes[br.breaksInMinutes.length-1]){
                return abs(br.classesInMinutes[0] + (1440-testTime));
            }
        }
        return null;
    }

}