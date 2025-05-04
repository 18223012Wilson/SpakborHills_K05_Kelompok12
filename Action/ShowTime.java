package Action;

import LocalCalendar.GameCalendar;

public class ShowTime extends Action {
    private GameCalendar calendar;

    // Constructor
    public ShowTime(GameCalendar calendar) {
        super("ShowTime", 
              "Memperlihatkan waktu (musim, hari, waktu)", 
              "None");
        if (calendar == null) {
            throw new IllegalArgumentException("GameCalendar tidak boleh null");
        }
        this.calendar = calendar;
    }

    @Override
    public void execute() {
        calendar.showTime();
    }

    @Override
    public int getEnergyCost() {
        return 0; // Tidak memerlukan energi
    }

    @Override
    public int getTimeCost() {
        return 0; // Aksi instan
    }

    // Main untuk tes
    public static void main(String[] args) {
        GameCalendar calendar = new GameCalendar();
        ShowTime showTimeAction = new ShowTime(calendar);
        
        System.out.println("Nama aksi: " + showTimeAction.getName());
        System.out.println("Deskripsi: " + showTimeAction.getDescription());
        System.out.println("Item dibutuhkan: " + showTimeAction.getItemNeeded());
        System.out.println("Biaya energi: " + showTimeAction.getEnergyCost());
        System.out.println("Biaya waktu: " + showTimeAction.getTimeCost());
        
        System.out.print("Eksekusi aksi: ");
        showTimeAction.execute();
    }
}