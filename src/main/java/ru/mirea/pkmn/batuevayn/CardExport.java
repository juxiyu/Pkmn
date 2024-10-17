package ru.mirea.pkmn.batuevayn;

import java.io.*;

public class CardExport {
    public CardExport(Card card) throws IOException {
        File myFile = new File("src/main/resources/" + card.getName() + ".crd");
        FileOutputStream fileOutputStream = new FileOutputStream(myFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(card);
    }
}
