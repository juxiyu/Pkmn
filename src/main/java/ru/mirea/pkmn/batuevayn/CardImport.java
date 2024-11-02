package ru.mirea.pkmn.batuevayn;
import ru.mirea.pkmn.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CardImport {
    public Card importCard(String filename) throws IOException {

        FileInputStream inputStream = new FileInputStream("src/main/resources/" + filename);
        Card card = new Card();
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(inputStreamReader))
        {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if(Objects.equals(parts[0], "1.")) {
                    card.setPokemonStage(PokemonStage.valueOf(parts[1]));
                } else if(Objects.equals(parts[0], "2.")) {
                    card.setName(parts[1]);
                } else if(Objects.equals(parts[0], "3.")) {
                    card.setHp(Integer.parseInt(parts[1]));
                } else if(Objects.equals(parts[0], "4.")) {
                    card.setPokemonType(EnergyType.valueOf(parts[1]));
                } else if(Objects.equals(parts[0], "5.")) {
                    if (parts.length == 2 && !parts[1].isEmpty() && !parts[1].equals("-")) {
                        Card evolvesCard = importCard(parts[1]);
                        card.setEvolvesFrom(evolvesCard);
                    } else {
                        card.setEvolvesFrom(null);
                    }
                } else if(Objects.equals(parts[0], "6.")) {
                    List<AttackSkill> attackSkills = new ArrayList<>();
                    if (parts[1].contains(",")) {
                        String[] skills = parts[1].split(",");
                        for (String skill : skills) {
                            String[] skillParts = skill.split("/");
                            attackSkills.add(new AttackSkill(skillParts[1], skillParts[0], Integer.parseInt(skillParts[2])));
                        }
                        card.setSkills(attackSkills);
                    } else if(parts[1].equals("-")) {
                        card.setSkills(null);
                    } else {
                        String[] skillParts = parts[1].split("/");
                        attackSkills.add(new AttackSkill(skillParts[1], skillParts[0], Integer.parseInt(skillParts[2])));
                        card.setSkills(attackSkills);
                    }
                } else if(Objects.equals(parts[0], "7.")){
                    card.setWeaknessType(parts.length == 2 && !parts[1].isEmpty() ? EnergyType.valueOf(parts[1]) : null);
                } else if(Objects.equals(parts[0], "8.")) {
                    card.setResistanceType(parts.length == 2 && !parts[1].isEmpty() && !parts[1].equals("-") ? EnergyType.valueOf(parts[1]) : null);
                } else if(Objects.equals(parts[0], "9.")) {
                    card.setRetreatCost(parts.length == 2 && !parts[1].isEmpty() && !parts[1].equals("-") ? parts[1] : null);
                } else if(Objects.equals(parts[0], "10.")) {
                    card.setGameSet(parts.length == 2 &&
                            !parts[1].isEmpty() &&
                            !parts[1].equals("-")
                            ? null
                            : String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)));
                } else if(Objects.equals(parts[0], "11.")) {
                    card.setRegulationMark(
                            parts.length == 2 && !parts[1].isEmpty()
                                    ? parts[1].charAt(0) : null);
                } else if(Objects.equals(parts[0], "12.")) {
                    if (parts.length == 2 && !parts[1].equals("-")) {
                        String[] ownerInfo = parts[1].split("/");

                        Student owner = new Student(ownerInfo[1], ownerInfo[0], ownerInfo[2], ownerInfo[3]);
                        card.setPokemonOwner(owner);
                    } else {
                        card.setPokemonOwner(null);
                    }
                } else if(Objects.equals(parts[0], "13.")){
                    card.setNumber(parts.length == 2 && !parts[1].isEmpty() && !parts[1].equals("-") ? parts[1] : null);
                }
            }
        }
        return card;
    }

    public Card deserializeCard(String name) throws IOException, ClassNotFoundException {
        String path = "src/main/resources/" + name + ".crd";
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (Card) objectInputStream.readObject();
    }
}