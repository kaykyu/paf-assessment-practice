package vttp.paf.assessmentpractice.repository;

public class Queries {
    
    public static final String SQL_GET_VACANCY = """
            select vacancy from acc_occupancy
                where acc_id = ?
            """;

    public static final String SQL_UPDATE_VACANCY = """
            update acc_occupancy
                set vacancy = ?
                where acc_id = ?
            """;

    public static final String SQL_SAVE_RESERVATION = """
            insert into reservations(resv_id, name, email, acc_id, arrival_date, duration)
                value (?, ?, ?, ?, ?, ?)
            """;
}
