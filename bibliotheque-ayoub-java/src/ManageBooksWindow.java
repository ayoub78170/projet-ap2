import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageBooksWindow extends JFrame {
    private JComboBox<String> livresCombo;
    private JTextField titreField;
    private JTextField prixField;
    private JComboBox<String> auteursCombo;
    private JTextField disponibiliteField;
    private String selectedid_livre;

    public ManageBooksWindow() {
        super("Gérer les Livres");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Fond violet
        panel.setBackground(new Color(148, 0, 211)); // Violet

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Sélectionner ou ajouter un livre"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        livresCombo = new JComboBox<>();
        loadBooksCombo();
        livresCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fillFieldsWithSelectedBook();
            }
        });
        panel.add(livresCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Titre:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        titreField = new JTextField(20);
        panel.add(titreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Prix:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        prixField = new JTextField(20);
        panel.add(prixField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Auteur:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        auteursCombo = new JComboBox<>();
        loadAuthorsCombo();
        panel.add(auteursCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Disponibilité:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        disponibiliteField = new JTextField(20);
        panel.add(disponibiliteField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JButton ajouterButton = new JButton("Ajouter");
        ajouterButton.setBackground(new Color(148, 0, 211)); // Violet
        ajouterButton.setForeground(Color.WHITE);
        ajouterButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        ajouterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ajouterLivre();
            }
        });
        panel.add(ajouterButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton modifierButton = new JButton("Modifier");
        modifierButton.setBackground(new Color(148, 0, 211)); // Violet
        modifierButton.setForeground(Color.WHITE);
        modifierButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        modifierButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifierLivre();
            }
        });
        panel.add(modifierButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        JButton supprimerButton = new JButton("Supprimer");
        supprimerButton.setBackground(new Color(148, 0, 211)); // Violet
        supprimerButton.setForeground(Color.WHITE);
        supprimerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        supprimerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                supprimerLivre();
            }
        });
        panel.add(supprimerButton, gbc);

        add(panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void loadAuthorsCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-ayoub", "root", "root");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT CONCAT(nom, ' ', prenom) AS auteur FROM auteur");

            while (rs.next()) {
                auteursCombo.addItem(rs.getString("auteur"));
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadBooksCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-ayoub", "root", "root");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT titre FROM livre");

            while (rs.next()) {
                livresCombo.addItem(rs.getString("titre"));
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fillFieldsWithSelectedBook() {
        String selectedBook = (String) livresCombo.getSelectedItem();
        if (selectedBook != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-ayoub", "root", "root");
                PreparedStatement stmt = con.prepareStatement("SELECT titre, prix, id_auteur, disponibilite, id_livre FROM livre WHERE titre = ?");
                stmt.setString(1, selectedBook);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    titreField.setText(rs.getString("titre"));
                    prixField.setText(rs.getString("prix"));
                    int id_auteur = rs.getInt("id_auteur");
                    auteursCombo.setSelectedItem(getAuteurName(id_auteur));
                    disponibiliteField.setText(String.valueOf(rs.getInt("disponibilite")));
                    selectedid_livre = rs.getString("id_livre");
                }

                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getAuteurName(int id_auteur) {
        String auteur = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-ayoub", "root", "root");
            PreparedStatement stmt = con.prepareStatement("SELECT CONCAT(nom, ' ', prenom) AS auteur FROM auteur WHERE id_auteur = ?");
            stmt.setInt(1, id_auteur);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                auteur = rs.getString("auteur");
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return auteur;
    }

    private void ajouterLivre() {
        String titre = titreField.getText();
        String prix = prixField.getText();
        String auteur = (String) auteursCombo.getSelectedItem();
        int disponibilite = Integer.parseInt(disponibiliteField.getText());

        if (titre.isEmpty() || prix.isEmpty() || auteur.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (checkDuplicateLivre(titre)) {
            JOptionPane.showMessageDialog(this, "Ce livre existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-ayoub", "root", "root");

            PreparedStatement insertStmt = con.prepareStatement("INSERT INTO livre (titre, prix, id_auteur, disponibilite) VALUES (?, ?, (SELECT id_auteur FROM auteur WHERE CONCAT(nom, ' ', prenom) = ?), ?)");
            insertStmt.setString(1, titre);
            insertStmt.setString(2, prix);
            insertStmt.setString(3, auteur);
            insertStmt.setInt(4, disponibilite);
            insertStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Livre ajouté avec succès.");
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean checkDuplicateLivre(String titre) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-ayoub", "root", "root");

            PreparedStatement stmt = con.prepareStatement("SELECT titre FROM livre WHERE titre = ?");
            stmt.setString(1, titre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                con.close();
                return true;
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void modifierLivre() {
        String titre = titreField.getText();
        String prix = prixField.getText();
        String auteur = (String) auteursCombo.getSelectedItem();
        int disponibilite = Integer.parseInt(disponibiliteField.getText());

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-ayoub", "root", "root");

            PreparedStatement updateStmt = con.prepareStatement("UPDATE livre SET titre = ?, prix = ?, id_auteur = (SELECT id_auteur FROM auteur WHERE CONCAT(nom, ' ', prenom) = ?), disponibilite = ? WHERE id_livre = ?");
            updateStmt.setString(1, titre);
            updateStmt.setString(2, prix);
            updateStmt.setString(3, auteur);
            updateStmt.setInt(4, disponibilite);
            updateStmt.setString(5, selectedid_livre);
            int rowsAffected = updateStmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Livre mis à jour avec succès.");
            } else {
                JOptionPane.showMessageDialog(this, "Aucun livre trouvé avec ce titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerLivre() {
        String titre = titreField.getText();

        if (titre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir le champ titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce livre ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-ayoub", "root", "root");

                PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM livre WHERE titre = ?");
                deleteStmt.setString(1, titre);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Livre supprimé avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun livre trouvé avec ce titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageBooksWindow());
    }
}