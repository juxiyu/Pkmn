package ru.mirea.pkmn.batuevayn;

import java.io.IOException;

public class PkmnApplication {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        CardImport cardImport= new CardImport("src/main/resources/my_card.txt");
        Card my_card = cardImport.getCard();
        System.out.println(my_card);

        CardExport cardExport = new CardExport(my_card);
        Card another_card = cardImport.exportCrd("src/main/resources/Pansage.crd");
        System.out.println(another_card);
    }
}
