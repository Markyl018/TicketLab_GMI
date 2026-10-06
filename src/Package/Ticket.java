package Package;

import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class Ticket {
    private String surname;      // фамилия читателя
    private String author;       // автор книги
    private String bookName;     // название книги
    private Date receiptDate;    // дата выдачи

    // статический массив объектов
    private static Ticket[] tickets;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");

    // регулярка для даты дд.ММ.гггг
    private static final Pattern DATE_PATTERN =
            Pattern.compile("^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.(\\d{4})$");

    // регулярки для проверки текстовых полей
    private static final Pattern HAS_DIGIT = Pattern.compile(".*\\d.*");
    private static final Pattern NAME_OK   = Pattern.compile("[А-Яа-яЁёA-Za-z\\-\\s]+");
    private static final Pattern AUTHOR_OK = Pattern.compile("[А-Яа-яЁёA-Za-z\\-\\s\\.]+");

    // статический блок инициализации
    static {
        SDF.setLenient(false);
        try {
            tickets = new Ticket[] {
                    new Ticket("Иванов", "Ефремов", "Таис Афинская", SDF.parse("15.05.2019")),
                    new Ticket("Петров", "Ефремов", "Лезвие бритвы",   SDF.parse("10.03.2020")),
                    new Ticket("Сидоров", "Толстой", "Война и мир",    SDF.parse("01.09.2021")),
                    new Ticket("Кузнецов", "Ефремов", "Туманность Андромеды", SDF.parse("20.05.2019"))
            };
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Конструкторы
    public Ticket(String surname, String author, String bookName, Date receiptDate) {
        this.surname = surname;
        this.author = author;
        this.bookName = bookName;
        this.receiptDate = receiptDate;
    }

    public Ticket() {
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        // Фамилия
        while (true) {
            System.out.print("Фамилия читателя : ");
            if (this.setSurname(sc.nextLine())) break;
            System.out.println("Повторите ввод фамилии.");
        }

        // Автор
        while (true) {
            System.out.print("\tАвтор : ");
            if (this.setAuthor(sc.nextLine())) break;
            System.out.println("Повторите ввод автора.");
        }

        // Название книги
        while (true) {
            System.out.print("\tНазвание книги : ");
            if (this.setBookName(sc.nextLine())) break;
            System.out.println("Повторите ввод названия книги.");
        }
        // Порог «месяц назад» (обнуляем время до 00:00)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date minDate = cal.getTime();

        // Порог «сегодня» (обнуляем время до 00:00)
        cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date maxDate = cal.getTime();

        // Дата — формат → существование → диапазон [minDate; maxDate]
        while (true) {
            System.out.print("\tДата выдачи книги (дд.ММ.гггг) : ");
            String line = sc.nextLine().trim();

            if (!DATE_PATTERN.matcher(line).matches()) {
                System.out.println("Неверный формат! Ожидается дд.ММ.гггг, например 15.05.2019");
                continue;
            }

            Date parsed;
            try {
                parsed = SDF.parse(line);
            } catch (ParseException e) {
                System.out.println("Такой даты не существует! Повторите ввод.");
                continue;
            }

            if (parsed.before(minDate)) {
                System.out.println("Дата не может быть раньше, чем " + SDF.format(minDate)
                        + " (месяц назад от текущей даты)");
                continue;
            }

            if (parsed.after(maxDate)) {
                System.out.println("Дата не может быть позже, чем " + SDF.format(maxDate)
                        + " (сегодняшняя дата)");
                continue;
            }

            this.setReceiptDate(parsed);
            break;
        }
    }

    @Override
    public String toString() {
        String d = (receiptDate == null) ? "—" : SDF.format(receiptDate);
        return String.format("Читатель: %-12s | Автор: %-12s | Книга: %-22s | Дата: %s",
                surname, author, bookName, d);
    }

    // Геттеры и сеттеры
    public String getSurname() { return surname; }

    public boolean setSurname(String surname) {
        if (surname == null) {
            System.out.println("Недопустимое значение фамилии (null)");
            return false;
        }
        surname = surname.trim();
        if (surname.length() < 2 || surname.length() > 20) {
            System.out.println("Недопустимое значение фамилии (длина 2–20 символов)");
            return false;
        }
        if (HAS_DIGIT.matcher(surname).matches()) {
            System.out.println("Фамилия не может содержать цифры");
            return false;
        }
        if (!NAME_OK.matcher(surname).matches()) {
            System.out.println("Фамилия может содержать только буквы, дефис и пробел");
            return false;
        }
        this.surname = surname;
        return true;
    }

    public String getAuthor() { return author; }

    public boolean setAuthor(String author) {
        if (author == null) {
            System.out.println("Недопустимое значение автора (null)");
            return false;
        }
        author = author.trim();
        if (author.length() < 2 || author.length() > 40) {
            System.out.println("Недопустимое значение автора (длина 2–40 символов)");
            return false;
        }
        if (HAS_DIGIT.matcher(author).matches()) {
            System.out.println("Автор не может содержать цифры");
            return false;
        }
        if (!AUTHOR_OK.matcher(author).matches()) {
            System.out.println("Автор может содержать только буквы, дефис, точку и пробел");
            return false;
        }
        this.author = author;
        return true;
    }

    public String getBookName() { return bookName; }

    public boolean setBookName(String bookName) {
        if (bookName == null) {
            System.out.println("Недопустимое значение названия (null)");
            return false;
        }
        bookName = bookName.trim();
        if (bookName.length() < 2 || bookName.length() > 40) {
            System.out.println("Недопустимое значение названия (длина 2–40 символов)");
            return false;
        }
        this.bookName = bookName;
        return true;
    }

    public Date getReceiptDate() {
        return receiptDate == null ? null : new Date(receiptDate.getTime());
    }
    public void setReceiptDate(Date date) {
        this.receiptDate = date == null ? null : new Date(date.getTime());
    }

    // 1) заполнение массива вручную
    public static void fillTickets() {
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
        int n;
        while (true) {
            System.out.print("Введите количество записей => ");
            String line = sc.nextLine().trim();
            try {
                n = Integer.parseInt(line);
                if (n <= 0) {
                    System.out.println("Число должно быть больше нуля. Повторите.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Это не число! Повторите.");
            }
        }
        //Ticket[] tickets2 = new Ticket[tickets.length + n];
        //System.arraycopy(tickets, 0, tickets2, 0, tickets.length);
        //tickets = new Ticket[tickets.length + n];
        int old_len = tickets.length;
        tickets = Arrays.copyOf(tickets, tickets.length + n);
        System.out.println("Введите информацию о читательских билетах:");
        for (int i = old_len; i < tickets.length; i++) {
            System.out.println("Запись " + (i + 1) + " =>");
            tickets[i] = new Ticket();
        }
    }

    // 2) печать массива
    public static void printTickets() {
        if (tickets == null || tickets.length == 0) {
            System.out.println("Массив пуст.");
            return;
        }
        System.out.println("\nСписок читательских билетов:");
        for (Ticket t : tickets) System.out.println(t);
    }

    // 3a) кто брал книгу Ефремова "Таис Афинская" 15.05.2019
    public static void whoTookThais() {
        try {
            Date target = SDF.parse("15.05.2019");
            System.out.println("\nКнигу Ефремова \"Таис Афинская\" 15.05.2019 брал(и):");
            boolean found = false;
            for (Ticket t : tickets) {
                if ("Ефремов".equalsIgnoreCase(t.getAuthor())
                        && "Таис Афинская".equalsIgnoreCase(t.getBookName())
                        && target.equals(t.getReceiptDate())) {
                    System.out.println("\t" + t.getSurname());
                    found = true;
                }
            }
            if (!found) System.out.println("\tНикто не найден.");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // 3b) количество книг заданного автора
    public static void countBooksByAuthor() {
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
        System.out.print("\nВведите автора => ");
        String a = sc.nextLine();
        int count = 0;
        for (Ticket t : tickets)
            if (a.equalsIgnoreCase(t.getAuthor())) count++;
        System.out.println("Количество книг автора \"" + a + "\": " + count);
    }

    // 3c) сортировка читателей по убыванию количества выданных книг
    public static void sortReadersByBookCount() {
        java.util.Map<String, Integer> counts = new java.util.LinkedHashMap<>();
        for (Ticket t : tickets)
            counts.merge(t.getSurname(), 1, Integer::sum);

        java.util.List<java.util.Map.Entry<String, Integer>> list =
                new java.util.ArrayList<>(counts.entrySet());
        list.sort((e1, e2) -> e2.getValue() - e1.getValue());

        System.out.println("\nЧитатели по убыванию количества выданных книг:");
        for (var e : list)
            System.out.printf("\t%-15s : %d%n", e.getKey(), e.getValue());
    }
}