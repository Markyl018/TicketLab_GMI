import Package.*;

import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        cycle:
        while (true) {
            System.out.println("\n1. Заполнить массив");
            System.out.println("2. Распечатать");
            System.out.println("3. Кто брал Ефремова \"Таис Афинская\" 15.05.2019");
            System.out.println("4. Количество книг заданного автора");
            System.out.println("5. Читатели по убыванию количества книг");
            System.out.println("6. Выход");
            System.out.print("Выберите пункт меню (1..6): ");

            int c;
            try {
                c = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Это не число! Повторите.");
                continue;
            }

            switch (c) {
                case 1: Ticket.fillTickets(); break;
                case 2: Ticket.printTickets(); break;
                case 3: Ticket.whoTookThais(); break;
                case 4: Ticket.countBooksByAuthor(); break;
                case 5: Ticket.sortReadersByBookCount(); break;
                default: break cycle;
            }
        }
        sc.close();
    }
}