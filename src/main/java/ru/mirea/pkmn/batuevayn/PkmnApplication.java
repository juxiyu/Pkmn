package ru.mirea.pkmn.batuevayn;
import ru.mirea.pkmn.Card;
import java.io.*;

public class PkmnApplication {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        CardImport cardImport = new CardImport();
        Card card = cardImport.importCard("my_card.txt");
        System.out.println("My pokemon" + '\n' + card);

        CardExport cardExport = new CardExport(card);
        cardExport.serializeToBytes();
        Card anotherCard = cardImport.deserializeCard("Starmie");
        System.out.println("Another pokemon" + '\n' + anotherCard);
    }
}
