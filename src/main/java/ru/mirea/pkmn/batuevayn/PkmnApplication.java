package ru.mirea.pkmn.batuevayn;
import com.fasterxml.jackson.databind.JsonNode;
import ru.mirea.pkmn.AttackSkill;
import ru.mirea.pkmn.Card;
import ru.mirea.pkmn.batuevayn.web.http.PkmnHttpClient;
import ru.mirea.pkmn.batuevayn.web.jdbc.DatabaseServiceImpl;

import java.io.*;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class PkmnApplication {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        CardImport cardImport = new CardImport();
        Card card = cardImport.importCard("my_card.txt");
        //System.out.println("My pokemon" + '\n' + card);

        //CardExport cardExport = new CardExport(card);
        //cardExport.serializeToBytes();
        //Card anotherCard = cardImport.deserializeCard("Starmie");
        //System.out.println("Another pokemon" + '\n' + anotherCard);

        DatabaseServiceImpl database = new DatabaseServiceImpl();
        database.createPokemonOwner(card.getPokemonOwner());
        database.saveCardToDatabase(card);
        System.out.println(database.getCardFromDatabase("Pansage"));
        System.out.println(database.getStudentFromDatabase("Batueva Yulia Nikolaevna"));
    }
}
