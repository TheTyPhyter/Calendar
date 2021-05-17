/*Programmer: Anthony Spears
This is a simple java program that takes a date given by the user,
and prints an accurate scalable calendar for the given date
For extra credit, I display the name of the month in ASCII art, and all menu options will work regardless of whether
or not the user has entered a specific date. this was accomplished by initializing the calendar instance to today's
date but not printing the calendar.

For Assignment 3 extra credit the user has the option to set the size of the calendar by manipulating the scale variable
The calendar events are also "aware" pf the scale variable and will print different lengths of the event depending on
the size of the calendar. If the calendar is large enough, the entire event name is printed, if not, the event
name is shortened
*/

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;



public class Assignment1 {
    public static void main(String[] args) throws ParseException, IOException {
        String events = "calendarEvents.txt";
        int scale = 15;
        Scanner scanner = new Scanner(System.in);
        String prompt = "Please enter a command: \n\"e\" to enter a date and display the corresponding calendar.\n" +
                "\"t\" to get today's calendar.\n\"n\" to display next month's calendar.\n" +
                "\"p\" to display the previous month's calendar.\n\"s\" to set the size of the calendar." +
                "\n\"ef\" to choose an event file.\n\"ev\" to add an event to the calendar.\n\"fp\" to print a " +
                "calendar to a file.\n\"q\" to quit the program";
        System.out.println(prompt);
        String input = scanner.nextLine();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        while(!input.equals("q")){
           if(input.equals("s")){
               System.out.println("Enter an integer between 0 and 50: ");
               scale = Integer.parseInt(scanner.nextLine());
           }else if(input.equals("ef")){
               System.out.println("Enter the the path to your events file (enter for default file)");
               String fileName = scanner.nextLine();
               if(fileName.length() > 0){
                   if (new File(fileName).exists()){
                       events = fileName;
                   }else{
                       throw new FileNotFoundException();
                   }
               }
           }else if (input.equals("ev")){
               System.out.println("Please enter an event in the format <mm/dd Name_of_Event>");
               String userEvent = scanner.nextLine();
               //correcting the formatting of user input.
               userEvent = userEvent.substring(0, 6) + userEvent.substring(6).replaceAll(" ", "_");
               //writing the user's events to the events files.
               Writer out = new FileWriter("calendarEvents.txt", true);
               out.write("\n"+userEvent);
               out.close();
           }else if (input.equals("fp")){
               calendar = menu("e", calendar);
               days(calendar, scale, events, true);
           }
           else {
               calendar = menu(input, calendar);
               days(calendar, scale, events, false);
           }
           System.out.println(prompt);
           input = scanner.nextLine();
        }

    }
    public static void days(Calendar calendar, int scale, String events, boolean filePrinting) throws IOException {//this function sets the height of cells
        String[][] eventsArray = new String[12][31];
        Scanner file = new Scanner(new File(events));
        while (file.hasNextLine()){
            String[] data = file.nextLine().split(" |/");
            eventsArray[Integer.parseInt(data[0])-1] [Integer.parseInt(data[1])-1] = data[2].replaceAll("_"," ");
        }
        int dw = calendar.get(Calendar.DAY_OF_WEEK) - 1; //this will tell us the day of the week that the first was/is
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String line = "";
        int n = dw + days < 36 ? (dw + days < 29 ? 4:5) : 6;//This line determines how many weeks we need in the month.

        line += asciiMonths(calendar.get(Calendar.MONTH));//print the name of the month that is currently displayed.
        for( int i = 0; i < n; i++){
            String spaces = "";
            for(int s = 0; s < scale; s++){
                spaces += " ";
            }
            line += topBox(scale);
            line += "Sun" + spaces + "Mon" + spaces + "Tue" + spaces + "Wed" +spaces + "Thu" + spaces + "Fri" + spaces + "Sat" + spaces + "|\n";

            for ( int j = 1; j < 8; j++){
                int num = j - dw + (i * 7);
                String holiday = num < 31 && num > 0 ? eventsArray[calendar.get(Calendar.MONTH)][num-1] : null;
                if(holiday != null){
                    holiday = " " + holiday;
                    if (holiday.length() > spaces.length()){//we check to see if the event will fit in our calender.
                        holiday = holiday.substring(0, spaces.length()-3) + "...";

                    }else{
                        holiday += spaces.substring(0, spaces.length()-holiday.length());

                    }
                    //we check how long the event name is, and make up the difference in spaces
                    // to keep our calendar format correct
                    line += num > 0 && num <= days ? "|" + (num) + (num > 9 ? holiday : holiday + " ") : "|  " + spaces;
                }else{
                    line += num > 0 && num <= days ? "|" + (num) + (num > 9 ? spaces : spaces + " ") : "|  " + spaces;
                }


            }
            line += "|\n";
            for (int k = 0; k < scale/2; k++){
                for (int l = 0; l < 7; l++){
                    line += "|  " + spaces;
                }
                line += "|\n";
            }
            line += topBox(scale);
        }
        if (filePrinting){//writing the calendar to a file instead of the console is the boolean "filePrinting" is true.
            Scanner pf = new Scanner(System.in);
            System.out.println("Enter a filename to print you calendar to:");
            String calendarFile = pf.nextLine();
            Writer out = new FileWriter(calendarFile);
            out.write(line);
            out.close();

        }else{//otherwise we output to the console.
            System.out.println(line);
        }

    }
    public static String topBox(int scale) { //this function prints the horizontal lines
        String line = "";
        for (int i = 0; i < 22 + scale * 7; i++) {
            line += "_";
        }
        line += "\n";
        return line;
    }
    public static Calendar menu(String input, Calendar calendar) throws ParseException {//this method is

        if (input.equals("e")){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter a date in the format mm/dd/yyyy: ");
            calendar.setTime(new SimpleDateFormat("MM/dd/yyyy").parse(scanner.nextLine()));

        }else if (input.equals("t")){
            calendar.setTime(new Date());

        }else if (input.equals("n")){
            calendar.add(Calendar.MONTH, 1);

        }else if (input.equals("p")) {
            calendar.add(Calendar.MONTH, -1);
        }
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));//sets calendar instance to first day pf the month
        return calendar;
    }
    public static String asciiMonths(int month){
        String[] asciiMonths = {
                "       _                                    \n" +
                        "      | |                                   \n" +
                        "      | | __ _ _ __  _   _  __ _ _ __ _   _ \n" +
                        "  _   | |/ _` | '_ \\| | | |/ _` | '__| | | |\n" +
                        " | |__| | (_| | | | | |_| | (_| | |  | |_| |\n" +
                        "  \\____/ \\__,_|_| |_|\\__,_|\\__,_|_|   \\__, |\n" +
                        "                                       __/ |\n" +
                        "                                      |___/ ",
                "  ______   _                                 \n" +
                        " |  ____| | |                                \n" +
                        " | |__ ___| |__  _ __ _   _  __ _ _ __ _   _ \n" +
                        " |  __/ _ \\ '_ \\| '__| | | |/ _` | '__| | | |\n" +
                        " | | |  __/ |_) | |  | |_| | (_| | |  | |_| |\n" +
                        " |_|  \\___|_.__/|_|   \\__,_|\\__,_|_|   \\__, |\n" +
                        "                                        __/ |\n" +
                        "                                       |___/ ",
                "  __  __                _     \n" +
                        " |  \\/  |              | |    \n" +
                        " | \\  / | __ _ _ __ ___| |__  \n" +
                        " | |\\/| |/ _` | '__/ __| '_ \\ \n" +
                        " | |  | | (_| | | | (__| | | |\n" +
                        " |_|  |_|\\__,_|_|  \\___|_| |_|",
                "                      _ _ \n" +
                        "     /\\              (_) |\n" +
                        "    /  \\   _ __  _ __ _| |\n" +
                        "   / /\\ \\ | '_ \\| '__| | |\n" +
                        "  / ____ \\| |_) | |  | | |\n" +
                        " /_/    \\_\\ .__/|_|  |_|_|\n" +
                        "          | |             \n" +
                        "          |_|             ",
                "  __  __             \n" +
                        " |  \\/  |            \n" +
                        " | \\  / | __ _ _   _ \n" +
                        " | |\\/| |/ _` | | | |\n" +
                        " | |  | | (_| | |_| |\n" +
                        " |_|  |_|\\__,_|\\__, |\n" +
                        "                __/ |\n" +
                        "               |___/ ",
                "       _                  \n" +
                        "      | |                 \n" +
                        "      | |_   _ _ __   ___ \n" +
                        "  _   | | | | | '_ \\ / _ \\\n" +
                        " | |__| | |_| | | | |  __/\n" +
                        "  \\____/ \\__,_|_| |_|\\___|",
                "       _       _       \n" +
                        "      | |     | |      \n" +
                        "      | |_   _| |_   _ \n" +
                        "  _   | | | | | | | | |\n" +
                        " | |__| | |_| | | |_| |\n" +
                        "  \\____/ \\__,_|_|\\__, |\n" +
                        "                  __/ |\n" +
                        "                 |___/ ",
                "                                _   \n" +
                        "     /\\                        | |  \n" +
                        "    /  \\  _   _  __ _ _   _ ___| |_ \n" +
                        "   / /\\ \\| | | |/ _` | | | / __| __|\n" +
                        "  / ____ \\ |_| | (_| | |_| \\__ \\ |_ \n" +
                        " /_/    \\_\\__,_|\\__, |\\__,_|___/\\__|\n" +
                        "                 __/ |              \n" +
                        "                |___/               ",
                "   _____            _                 _               \n" +
                        "  / ____|          | |               | |              \n" +
                        " | (___   ___ _ __ | |_ ___ _ __ ___ | |__   ___ _ __ \n" +
                        "  \\___ \\ / _ \\ '_ \\| __/ _ \\ '_ ` _ \\| '_ \\ / _ \\ '__|\n" +
                        "  ____) |  __/ |_) | ||  __/ | | | | | |_) |  __/ |   \n" +
                        " |_____/ \\___| .__/ \\__\\___|_| |_| |_|_.__/ \\___|_|   \n" +
                        "             | |                                      \n" +
                        "             |_|                                      ",
                "   ____       _        _               \n" +
                        "  / __ \\     | |      | |              \n" +
                        " | |  | | ___| |_ ___ | |__   ___ _ __ \n" +
                        " | |  | |/ __| __/ _ \\| '_ \\ / _ \\ '__|\n" +
                        " | |__| | (__| || (_) | |_) |  __/ |   \n" +
                        "  \\____/ \\___|\\__\\___/|_.__/ \\___|_|   ",
                "  _   _                          _               \n" +
                        " | \\ | |                        | |              \n" +
                        " |  \\| | _____   _____ _ __ ___ | |__   ___ _ __ \n" +
                        " | . ` |/ _ \\ \\ / / _ \\ '_ ` _ \\| '_ \\ / _ \\ '__|\n" +
                        " | |\\  | (_) \\ V /  __/ | | | | | |_) |  __/ |   \n" +
                        " |_| \\_|\\___/ \\_/ \\___|_| |_| |_|_.__/ \\___|_|   ",
                "  _____                          _               \n" +
                        " |  __ \\                        | |              \n" +
                        " | |  | | ___  ___ ___ _ __ ___ | |__   ___ _ __ \n" +
                        " | |  | |/ _ \\/ __/ _ \\ '_ ` _ \\| '_ \\ / _ \\ '__|\n" +
                        " | |__| |  __/ (_|  __/ | | | | | |_) |  __/ |   \n" +
                        " |_____/ \\___|\\___\\___|_| |_| |_|_.__/ \\___|_|   "
        };
        return asciiMonths[month]+ "\n";
    }
}




