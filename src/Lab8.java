import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Lab8 {

    /**
     * Gets the Line count of the file and divides it by 2 to create the array for the meeting objects.
     *
     * @param path a path to a file.
     * @return a long int representing the amount of lines in the file.
     */
    public static long getLineCount(Path path) {
        long lineCount = 0;
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            lineCount = stream.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lineCount;
    }

    /**
     * Reads the file and returns an array of meetings.
     *
     * @param filename a string to a file path.
     * @return an array of meetings, unsorted by any criteria.
     */
    public static Meeting[] readFile(String filename) {
        File file          = new File(filename);
        Path path          = Paths.get(filename);
        long lineCount     = getLineCount(path);
        Meeting[] schedule = new Meeting[(int) lineCount / 2];

        try {
            Scanner scanner = new Scanner(file);
            int i           = 0;
            String customer = null;
            String time     = null;
            int index       = 0;

            while (scanner.hasNextLine()) {
                String next = scanner.nextLine();

                if (i % 2 == 1) {
                    time = next;
                    index = processLines(customer, time, schedule, index);
                } else {
                    customer = next;
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return schedule;
    }

    /**
     * Performs a bubble sort on the meetings based on start times, whereas the earliest start time is
     * first in the array.
     *
     * @param schedule an array of meetings to sort.
     * @return a sorted array of meetings by the start times of the meetings
     */
    public static Meeting[] bubbleSortArray(Meeting[] schedule) {

        for (int i = 0; i < schedule.length; i++) {
            for (int y = i + 1; y < schedule.length; y++) {
                if (schedule[y].getStart() < schedule[i].getStart()) {
                    Meeting temp = schedule[i];
                    schedule[i]  = schedule[y];
                    schedule[y]  = temp;
                }
            }
        }
        System.out.println(Arrays.toString(schedule));
        return schedule;
    }

    public static Meeting[] bubbleSortArrayEndTimes(Meeting[] schedule) {

        for (int i = 0; i < schedule.length; i++) {
            for (int y = i + 1; y < schedule.length; y++) {
                if (schedule[y].getEnd() < schedule[i].getEnd()) {
                    Meeting temp = schedule[i];
                    schedule[i]  = schedule[y];
                    schedule[y]  = temp;
                }
            }
        }
        System.out.println(Arrays.toString(schedule));
        return schedule;
    }

    public static ArrayList<Meeting> rescheduleMeetingsTime(Meeting[] schedule) {
        ArrayList<Meeting> revisedSchedule = new ArrayList<>();
        revisedSchedule.add(schedule[0]); // add the first one to the list.

        int index = 0;
        for (int y = 0; y < schedule.length - 1; y++) {
            if (schedule[y + 1].getStart() >= revisedSchedule.get(index).getEnd()) {
                revisedSchedule.add(schedule[y + 1]);
                index++;
            }
        }
        return revisedSchedule;
    }

    public static ArrayList<Meeting> rescheduleMeetingsLength(Meeting[] schedule) {
        ArrayList<Meeting> revisedSchedule = new ArrayList<>();
        revisedSchedule.add(schedule[0]); // add the first one to the list.

        System.out.println("\n\n");
        for (int i = 1; i < schedule.length; i++) { // each item in the schedule.
            boolean conflicting = false;

            for (Meeting meeting : revisedSchedule) {
                if (meeting.overlapsWith(schedule[i])) {
                    conflicting = true;
                    break;
                }
            }
            if(!conflicting){
                revisedSchedule.add(schedule[i]);
            }
        }
        return revisedSchedule;
    }


    public static ArrayList<Meeting> rankByStart(Meeting[] schedule) {

        Meeting[] sortedSchedule = bubbleSortArray(schedule);
        return rescheduleMeetingsTime(sortedSchedule);
    }


    public static ArrayList<Meeting> rankByEndTime(Meeting[] schedule){
        Meeting[] sortedSchedule = bubbleSortArrayEndTimes(schedule);
        return rescheduleMeetingsEndTime(sortedSchedule);

    }

    public static ArrayList<Meeting> rescheduleMeetingsEndTime(Meeting[] schedule){
        ArrayList<Meeting> revisedSchedule = new ArrayList<>();
        revisedSchedule.add(schedule[0]); // add the first one to the list.

        int index = 0;
        for (int y = 0; y < schedule.length - 1; y++) {
            if (!schedule[y + 1].overlapsWith(revisedSchedule.get(index))) {
                revisedSchedule.add(schedule[y + 1]);
                index++;
            }
        }
        return revisedSchedule;
    }

    public static ArrayList<Meeting> rankByLength(Meeting[] schedule) {
        Meeting smallestLen;
        boolean change = false;
        int smallestIndex = 0;
        for (int i = 0; i < schedule.length; i++) {
            smallestLen = schedule[i];

            for (int y = i + 1; y < schedule.length; y++) {
                if (smallestLen.getLength() >= schedule[y].getLength()) {
                    smallestLen = schedule[y];
                    smallestIndex = y;
                    change = true;
                }
            }
            if (change) {
                Meeting temp = schedule[i];
                schedule[i] = smallestLen;
                schedule[smallestIndex] = temp;
                change = false;
            }
        }

        System.out.println(Arrays.toString(schedule));

        return rescheduleMeetingsLength(schedule);
    }


    private static int processLines(String customer, String time, Meeting[] schedule, int index) {

        String[] timesRequested = time.split(" ");
        int startTime = Integer.parseInt(timesRequested[0]);
        int endTime = Integer.parseInt(timesRequested[1]);
        Meeting meeting = new Meeting(customer, startTime, endTime);
        schedule[index] = meeting;

        return index + 1;
    }

    public static void main(String[] args) {
        ArrayList<Meeting> meetingsS1;
        ArrayList<Meeting> meetingsS2;
        ArrayList<Meeting> meetingsS3;
        ArrayList<Meeting> meetingsS4;

        ArrayList<Meeting> meetingsL1;
        ArrayList<Meeting> meetingsL2;
        ArrayList<Meeting> meetingsL3;
        ArrayList<Meeting> meetingsL4;

        ArrayList<Meeting> meetingsT7;
        ArrayList<Meeting> meetingsT8;
        ArrayList<Meeting> meetingsT9;
        ArrayList<Meeting> meetingsT10;


//        Meeting[] scheduleS1  = readFile("data1.txt");
//        Meeting[] scheduleS2 = readFile("data2.txt");
//        Meeting[] scheduleS3 = readFile("data3.txt");
//        Meeting[] scheduleS4 = readFile("data4.txt");
//
//        meetingsS1 = rankByStart(scheduleS1);
//        meetingsS2 = rankByStart(scheduleS2);
//        meetingsS3 = rankByStart(scheduleS3);
//        meetingsS4= rankByStart(scheduleS4);
//
//
//        System.out.println("Data 1 START TIME-----");
//        System.out.println(meetingsS1);
//        System.out.println(meetingsS1.size());
//
//        System.out.println("Data 2 START TIME-----");
//        System.out.println(meetingsS2);
//        System.out.println(meetingsS2.size());
//
//        System.out.println("Data 3 START TIME-----");
//        System.out.println(meetingsS3);
//        System.out.println(meetingsS3.size());
//
//        System.out.println("Data 4 START TIME-----");
//        System.out.println(meetingsS4);
//        System.out.println(meetingsS4.size());

//
//        Meeting[] scheduleL1 = readFile("data1.txt");
//        Meeting[] scheduleL2 = readFile("data2.txt");
//        Meeting[] scheduleL3 = readFile("data3.txt");
//        Meeting[] scheduleL4 = readFile("data4.txt");
//
//        meetingsL1 = rankByLength(scheduleL1);
//        meetingsL2 = rankByLength(scheduleL2);
//        meetingsL3 = rankByLength(scheduleL3);
//        meetingsL4 = rankByLength(scheduleL4);
//
//        System.out.println("\n\n");
//
//        System.out.println("Data 1 By LENGTH-----");
//        System.out.println(meetingsL1);
//        System.out.println(meetingsL1.size());
//        System.out.println("Data 2 By LENGTH-----");
//        System.out.println(meetingsL2);
//        System.out.println(meetingsL2.size());
//
//        System.out.println("Data 3 By LENGTH-----");
//        System.out.println(meetingsL3);
//        System.out.println(meetingsL3.size());
//
//        System.out.println("Data 4 By LENGTH-----");
//        System.out.println(meetingsL4);
//        System.out.println(meetingsL4.size());

        Meeting[] scheduleTE1 = readFile("data1.txt");
        Meeting[] scheduleTE2 = readFile("data2.txt");
        Meeting[] scheduleTE3 = readFile("data3.txt");
        Meeting[] scheduleTE4 = readFile("data4.txt");

        meetingsT7 = rankByEndTime(scheduleTE1);
        meetingsT8 = rankByEndTime(scheduleTE2);
        meetingsT9 = rankByEndTime(scheduleTE3);
        meetingsT10 = rankByEndTime(scheduleTE4);


        System.out.println("Data 1 By LENGTH-----");
        System.out.println(meetingsT7);
        System.out.println(meetingsT7.size());
        System.out.println("Data 2 By LENGTH-----");
        System.out.println(meetingsT8);
        System.out.println(meetingsT8.size());

        System.out.println("Data 3 By LENGTH-----");
        System.out.println(meetingsT9);
        System.out.println(meetingsT9.size());

        System.out.println("Data 4 By LENGTH-----");
        System.out.println(meetingsT10);
        System.out.println(meetingsT10.size());

    }
}
