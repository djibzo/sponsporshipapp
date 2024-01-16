package sn.dev.sponsorshipapp.entities;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class Parrainer {
    private int id;
    private int idUser;
    private int idCandidat;
}
