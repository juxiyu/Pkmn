package ru.mirea.pkmn.batuevayn;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CardImport {
    private Card card;
    public CardImport(String filename) {
        try {
            String[] lines = readLines(filename);
            card = new Card();
            card = processLines(lines);
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Card getCard() {
        return card;
    }

    private void setCard(Card card) {
        this.card = card;
    }

    public Card exportCrd(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Card card = (Card) objectInputStream.readObject();
        return card;
    }

    private String[] readLines(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String[] lines = new String[15];
        String line;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            lines[i] = line;
            i++;
        }
        return lines;
    }

    private Card processLines(String[] lines) throws IOException {
        card.setPokemonStage(stage(lines[0]));
        card.setName(lines[1]);
        card.setHp(Integer.parseInt(lines[2]));
        card.setPokemonType(type(lines[3]));

        if (lines[4].equals("-")) {
            card.setEvolvesFrom(null);
        }
        else {
            CardImport cardEvolves = new CardImport(lines[4]);
            card.setEvolvesFrom(cardEvolves.getCard());
        }

        AttackSkill attackSkill;
        ArrayList<AttackSkill> skill = new ArrayList<>();
        String[] skills = lines[5].split("/");
        for(int i = 0; i < skills.length; i += 3){
            attackSkill = new AttackSkill();
            attackSkill.setCost(skills[i]);
            attackSkill.setName(skills[i+1]);
            attackSkill.setDamage(Integer.parseInt(skills[i+2]));
            skill.add(i/3, attackSkill);
        }
        card.setSkills(skill);

        card.setWeaknessType(type(lines[6]));
        card.setResistanceType(type(lines[7]));
        card.setRetreatCost(lines[8]);
        card.setGameSet(lines[9]);
        card.setRegulationMark(lines[10].charAt(0));

        String[] ownerLine= lines[11].split("/");
        Student owner = new Student(ownerLine[1], ownerLine[0], ownerLine[2], ownerLine[3]);
        card.setPokemonOwner(owner);

        return card;
    }

    private PokemonStage stage(String s) {
        if (s.equals("BASIC"))
            return PokemonStage.BASIC;
        else if (s.equals("STAGE1"))
            return PokemonStage.STAGE1;
        else if (s.equals("STAGE2"))
            return PokemonStage.STAGE2;
        else if (s.equals("VSTAR"))
            return PokemonStage.VSTAR;
        else if (s.equals("VMAX"))
            return PokemonStage.VMAX;
        return null;
    }

    private EnergyType type(String s) {
        if (s.equals("FIRE"))
            return EnergyType.FIRE;
        else if (s.equals("GRASS"))
            return EnergyType.GRASS;
        else if (s.equals("WATER"))
            return EnergyType.WATER;
        else if (s.equals("LIGHTNING"))
            return EnergyType.LIGHTNING;
        else if (s.equals("PSYCHIC"))
            return EnergyType.PSYCHIC;
        else if (s.equals("FIGHTING"))
            return EnergyType.FIGHTING;
        else if (s.equals("DARKNESS"))
            return EnergyType.DARKNESS;
        else if (s.equals("METAL"))
            return EnergyType.METAL;
        else if (s.equals("FAIRY"))
            return EnergyType.FAIRY;
        else if (s.equals("DRAGON"))
            return EnergyType.DRAGON;
        else if (s.equals("COLORLESS"))
            return EnergyType.COLORLESS;
        return null;
    }
}
