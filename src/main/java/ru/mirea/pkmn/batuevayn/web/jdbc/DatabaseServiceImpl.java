package ru.mirea.pkmn.batuevayn.web.jdbc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.mirea.pkmn.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;

public class DatabaseServiceImpl implements DatabaseService {

    private final Connection connection;

    private final Properties databaseProperties;

    public DatabaseServiceImpl() throws SQLException, IOException {

        databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream("src/main/resources/database.properties"));

        connection = DriverManager.getConnection(
                databaseProperties.getProperty("database.url"),
                databaseProperties.getProperty("database.user"),
                databaseProperties.getProperty("database.password")
        );

        System.out.println("Connection is " + (connection.isValid(0) ? "up" : "down"));
    }


    private Card getCardFromDatabaseById(UUID uuid) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from card where id = ?");
        preparedStatement.setObject(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Card card = new Card();
            card.setName(resultSet.getString("name"));
            card.setHp(resultSet.getInt("hp"));

            if(resultSet.getString("evolves_from") != null) {
                card.setEvolvesFrom(getCardFromDatabaseById(UUID.fromString(resultSet.getString("evolves_from"))));
            } else {
                card.setEvolvesFrom(null);
            }

            card.setGameSet(resultSet.getString("game_set"));

            if(resultSet.getString("pokemon_owner") != null) {
                card.setPokemonOwner(getStudentFromDatabase(resultSet.getString("pokemon_owner")));
            } else
                card.setPokemonOwner(null);

            card.setPokemonStage(PokemonStage.valueOf(resultSet.getString("stage")));

            String value;
            if((value = resultSet.getString("retreat_cost")) != null) {
                card.setRetreatCost(value);
            } else card.setRetreatCost(null);

            if((value = resultSet.getString("weakness_type")) != null) {
                card.setWeaknessType(EnergyType.valueOf(value));
            } else card.setWeaknessType(null);

            if((value = resultSet.getString("resistance_type")) != null) {
                card.setResistanceType(EnergyType.valueOf(value));
            } else card.setResistanceType(null);

            Gson gson = new Gson();
            Type type = new TypeToken<List<AttackSkill>>() {}.getType();
            List<AttackSkill> skills = gson.fromJson(resultSet.getString("attack_skills"), type);
            card.setSkills(skills);

            card.setPokemonType(EnergyType.valueOf(resultSet.getString("pokemon_type")));

            if((value = resultSet.getString("regulation_mark")) != null) {
                card.setRegulationMark(value.charAt(0));
            } else card.setRegulationMark(null);

            card.setNumber(resultSet.getString("card_number"));
            preparedStatement.close();
            return card;
        }
        preparedStatement.close();
        return null;
    }

    public Student getStudentFromDatabaseById(UUID uuid) throws SQLException {
        String query = "select * from card where \"id\" = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Student student = new Student();

                String value;
                if ((value = resultSet.getString("firstName")) != null) {
                    student.setFirstName(value);
                } else student.setFirstName(null);

                if ((value = resultSet.getString("familyName")) != null) {
                    student.setSurName(value);
                } else student.setSurName(null);

                if ((value = resultSet.getString("patronicName")) != null) {
                    student.setFamilyName(value);
                } else student.setFamilyName(null);

                if ((value = resultSet.getString("group")) != null) {
                    student.setGroup(value);
                } else student.setGroup(null);

                return student;
            } else return null;
        }
    }

    @Override
    public Card getCardFromDatabase(String cardName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from card where name = ?");
        preparedStatement.setString(1, cardName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Card card = new Card();
            card.setName(resultSet.getString("name"));
            card.setHp(resultSet.getInt("hp"));

            if(resultSet.getString("evolves_from") != null) {
                card.setEvolvesFrom(getCardFromDatabaseById(UUID.fromString(resultSet.getString("evolves_from"))));
            } else {
                card.setEvolvesFrom(null);
            }

            card.setGameSet(resultSet.getString("game_set"));

            if(resultSet.getString("pokemon_owner") != null) {
                card.setPokemonOwner(getStudentFromDatabaseById(UUID.fromString(resultSet.getString("pokemon_owner"))));
            } else
                card.setPokemonOwner(null);

            card.setPokemonStage(PokemonStage.valueOf(resultSet.getString("stage")));

            String value;
            if((value = resultSet.getString("retreat_cost")) != null) {
                card.setRetreatCost(value);
            } else card.setRetreatCost(null);

            if((value = resultSet.getString("weakness_type")) != null) {
                card.setWeaknessType(EnergyType.valueOf(value));
            } else card.setWeaknessType(null);

            if((value = resultSet.getString("resistance_type")) != null) {
                card.setResistanceType(EnergyType.valueOf(value));
            } else card.setResistanceType(null);

            Gson gson = new Gson();
            Type type = new TypeToken<List<AttackSkill>>() {}.getType();
            List<AttackSkill> skills = gson.fromJson(resultSet.getString("attack_skills"), type);
            card.setSkills(skills);

            card.setPokemonType(EnergyType.valueOf(resultSet.getString("pokemon_type")));

            if((value = resultSet.getString("regulation_mark")) != null) {
                card.setRegulationMark(value.charAt(0));
            } else card.setRegulationMark(null);

            card.setNumber(resultSet.getString("card_number"));
            preparedStatement.close();
            return card;
        }
        preparedStatement.close();
        return null;
    }


    @Override
    public Student getStudentFromDatabase(String studentName) throws SQLException {
        if (studentName==null){
            return null;
        }
        String[] split = studentName.split(" ");
        PreparedStatement preparedStatement = connection.prepareStatement("select * from student where " + "\"familyName\" = ? and \"firstName\" = ? and \"patronicName\" = ?");
        preparedStatement.setString(1, split[0]);
        preparedStatement.setString(2, split[1]);
        preparedStatement.setString(3, split[2]);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Student student = new Student(
                    resultSet.getString("firstName"),
                    resultSet.getString("familyName"),
                    resultSet.getString("patronicName"),
                    resultSet.getString("group")
            );
            preparedStatement.close();
            return student;
        }
        preparedStatement.close();
        return null;
    }

    private UUID getCardIdFromDatabase(String cardName) throws SQLException {
        String query = "select * from card where \"name\" = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, cardName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            return UUID.fromString(resultSet.getString("id"));
        }
        preparedStatement.close();
        return null;
    }

    private UUID getStudentIdFromDatabase(String studentName) throws SQLException {
        String query = "select * from student where \"familyName\" = ? and \"firstName\" = ? and \"patronicName\" = ?";
        String[] split = studentName.split(" ");
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, split[0]);
        preparedStatement.setString(2, split[1]);
        preparedStatement.setString(3, split[2]);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return UUID.fromString(resultSet.getString("id"));
        }
        return null;
    }

    @Override
    public void saveCardToDatabase(Card card) throws SQLException {
        Card evolvesFrom;
        UUID evolvesFromId = null;
        if ((evolvesFrom = card.getEvolvesFrom()) != null) {
            if (getCardFromDatabase(evolvesFrom.getName()) == null) {
                saveCardToDatabase(evolvesFrom);
            }
            evolvesFromId = getCardIdFromDatabase(evolvesFrom.getName());
        }

        Student pokemonOwner;
        UUID ownerId = null;
        if ((pokemonOwner = card.getPokemonOwner()) != null) {
            if (getStudentFromDatabase(pokemonOwner.getSurName() + " " + pokemonOwner.getFirstName() + " " + pokemonOwner.getFamilyName()) == null) {
                createPokemonOwner(card.getPokemonOwner());
            }
            ownerId = getStudentIdFromDatabase(pokemonOwner.getSurName() + " " + pokemonOwner.getFirstName() + " " + pokemonOwner.getFamilyName());
        }

        String query = "insert into card(id, name, hp, evolves_from, " +
                "game_set, pokemon_owner, stage, retreat_cost, " +
                "weakness_type, resistance_type, attack_skills, " +
                "pokemon_type, regulation_mark, card_number) VALUES(" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?::json, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, UUID.randomUUID());
            preparedStatement.setString(2, card.getName());
            preparedStatement.setInt(3, card.getHp());
            preparedStatement.setObject(4, evolvesFromId);
            preparedStatement.setString(5, card.getGameSet());
            preparedStatement.setObject(6, ownerId);
            preparedStatement.setString(7, card.getPokemonStage().name());
            preparedStatement.setString(8, card.getRetreatCost());

            if (card.getWeaknessType() != null) {
                preparedStatement.setString(9, card.getWeaknessType().name());
            } else {
                preparedStatement.setString(9, null);
            }

            if (card.getResistanceType() != null) {
                preparedStatement.setString(10, card.getResistanceType().name());
            } else {
                preparedStatement.setString(10, null);
            }

            preparedStatement.setString(11, new Gson().toJson(card.getSkills()));
            preparedStatement.setString(12, card.getPokemonType().name());
            preparedStatement.setString(13, String.valueOf(card.getRegulationMark()));
            preparedStatement.setString(14, card.getNumber());
            preparedStatement.execute();
        }
    }

    @Override
    public void createPokemonOwner(Student owner) throws SQLException {
        String query = "insert into student(id, \"familyName\", \"firstName\", \"patronicName\", \"group\") " + "values(?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setObject(1, UUID.randomUUID());
        preparedStatement.setString(2, owner.getSurName());
        preparedStatement.setString(3, owner.getFirstName());
        preparedStatement.setString(4, owner.getFamilyName());
        preparedStatement.setString(5, owner.getGroup());
        preparedStatement.execute();
        preparedStatement.close();
    }
}

