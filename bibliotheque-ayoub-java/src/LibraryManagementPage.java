import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LibraryManagementPage extends JFrame {
    public LibraryManagementPage() {
        super("Gérer la Bibliothèque");

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(150, 90, 255)); // Violet

        JButton gererAdherentsButton = createButton("Gérer les Adhérents");
        gererAdherentsButton.addActionListener(e -> openManageAdherentWindow());
        panel.add(gererAdherentsButton);

        JButton gererLivresButton = createButton("Gérer les Livres");
        gererLivresButton.addActionListener(e -> openManageBooksWindow());
        panel.add(gererLivresButton);

        JButton gererAuteursButton = createButton("Gérer les Auteurs");
        gererAuteursButton.addActionListener(e -> openManageAuthorsWindow());
        panel.add(gererAuteursButton);

        JButton emprunterLivreButton = createButton("Emprunter un Livre");
        emprunterLivreButton.addActionListener(e -> openEmpruntLivreWindow());
        panel.add(emprunterLivreButton);

        add(panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(200, 160, 255)); // Violet plus clair
        button.setForeground(Color.WHITE); // Texte en blanc
        button.setFocusPainted(false); // Enlève le contour de focus
        button.setBorderPainted(false); // Enlève le contour du bouton
        button.setOpaque(true); // Permet de peindre le bouton
        return button;
    }

    private void openManageAdherentWindow() {
        new ManageAdherentWindow();
    }

    private void openManageBooksWindow() {
        new ManageBooksWindow();
    }

    private void openManageAuthorsWindow() {
        new ManageAuthorsWindow();
    }

    private void openEmpruntLivreWindow() {
        new EmpruntLivreWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryManagementPage());
    }
}