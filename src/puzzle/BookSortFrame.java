package puzzle;

import main.GamePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BookSortFrame extends JDialog {

    private final GamePanel gp;
    private final boolean sortByWidth;
    public  JPanel libraryPanel;
    private JLabel uparrowLabel;
    private JLabel leftarrowLabel; 
    private JLabel rightarrowLabel;
    private int selectedBookIndex = 0;
    private int[] bookXPositions;
    private int upArrowBaseY = 60 + 190;
    private int totalBooks = 9; 
    private JLabel shelfLabel;
    private java.util.List<JLabel> bookLabels = new java.util.ArrayList<>();
         // Books
    private int[][] bookSizes = {
        {64, 159}, {35, 117}, {45, 152}, {95, 167},
        {57, 143}, {58, 135}, {64, 168}, {34, 126}, {57, 96}
    };
    private PuzzleMessageBox messageBox;
    private boolean inputEnabled;

    public BookSortFrame(GamePanel gp, boolean sortByWidth) {
        super(SwingUtilities.getWindowAncestor(gp), Dialog.ModalityType.APPLICATION_MODAL);
        this.sortByWidth = sortByWidth;
        if (sortByWidth) bookSizes = new int[][] {
            {53, 137}, {32, 122}, {92, 150}, {41, 144},
            {72, 152}, {27, 136}, {47, 154}, {84, 150}, {56, 144}
        };
        this.gp = gp;
        setTitle("Book Sorting Game");
        setSize(648, 470); 
        setLocationRelativeTo(gp);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "move-left");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "move-right");
        getRootPane().getActionMap().put("move-left", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { if (inputEnabled) moveBookLeft(); }
        });
        getRootPane().getActionMap().put("move-right", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { if (inputEnabled) moveBookRight(); }
        });

        setFocusable(true);
        
        // Background Panel
        libraryPanel = new BackgroundPanel("/librarygame/LibraryBackground.png");
        libraryPanel.setPreferredSize(new Dimension(648, 416));
        libraryPanel.setLayout(null); 
        
        ImageIcon shelfIcon = new ImageIcon(getClass().getResource("/librarygame/bookshelf.png"));
        shelfLabel = new JLabel(shelfIcon);
        shelfLabel.setBounds(60, 60, 528, 168); 
        libraryPanel.add(shelfLabel);
        
        
        ImageIcon leftarrowIcon = new ImageIcon(getClass().getResource("/librarygame/leftarrow.png"));
        leftarrowLabel = new JLabel(leftarrowIcon);
        leftarrowLabel.setBounds(232, 304, 80, 72); 
        libraryPanel.add(leftarrowLabel);
        
        ImageIcon rightarrowIcon = new ImageIcon(getClass().getResource("/librarygame/rightarrow.png"));
        rightarrowLabel = new JLabel(rightarrowIcon);
        rightarrowLabel.setBounds(334, 304, 80, 72); 
        libraryPanel.add(rightarrowLabel);
                
        ImageIcon uparrowIcon = new ImageIcon(getClass().getResource("/librarygame/up_arrow.png"));
        uparrowLabel = new JLabel(uparrowIcon); 

        


    bookXPositions = new int[bookSizes.length * 2];

    int currentX = 9;
    for (int i = 0; i < bookSizes.length; i++) {
        int width = bookSizes[i][0];
        int height = bookSizes[i][1];

        ImageIcon bookIcon = new ImageIcon(getClass().getResource((sortByWidth ? "/librarygame/books" : "/librarygame/book") + (i + 1) + ".png"));
        JLabel bookLabel = new JLabel(bookIcon);
        bookLabels.add(bookLabel);
        int x = currentX;
        int y = shelfLabel.getHeight() - height; 

        bookLabel.setBounds(x, y, width, height);
        shelfLabel.add(bookLabel);

        bookXPositions[i * 2] = x;
        bookXPositions[i * 2 + 1] = width;      

        currentX += width;
    }

    
    int firstBookX = bookXPositions[0];
    int firstBookWidth = bookXPositions[1];
    int arrowWidth = uparrowIcon.getIconWidth();
    int xCenter = 60 + firstBookX + (firstBookWidth - arrowWidth) / 2;
    uparrowLabel.setBounds(xCenter, upArrowBaseY, arrowWidth, uparrowIcon.getIconHeight());
    libraryPanel.add(uparrowLabel);

    
    
     leftarrowLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!inputEnabled) return;
                gp.playSound(sound.SoundEffects.Cue.CLICK);
                if (selectedBookIndex > 0) {
                    selectedBookIndex--;
                } else if (selectedBookIndex == 0) {
                    selectedBookIndex = totalBooks - 1; 
                }
                updateUpArrowPosition();
            }
        });

        rightarrowLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!inputEnabled) return;
                gp.playSound(sound.SoundEffects.Cue.CLICK);
                if (selectedBookIndex < totalBooks - 1) {
                    selectedBookIndex++;
                } else if (selectedBookIndex == totalBooks - 1) {
                    selectedBookIndex = 0; // Wrap to the first book
                }
                updateUpArrowPosition();
            }
        });
        
            add(libraryPanel, BorderLayout.CENTER);


        messageBox = new PuzzleMessageBox(new String[]{
            "Click the left and right arrows to select a \nbook.",
            "Press A to move the selected book to the left \nand D to move the selected book to the right.",
            sortByWidth ? "Sort the books by width!" : "Sort the books by height!"
        });

        messageBox.setOnButtonClick(() -> gp.playSound(sound.SoundEffects.Cue.CLICK));
        messageBox.setOnFinishListener(() -> {
            setKeyInputEnabled(true);
        });

        messageBox.setVisible(true);
        libraryPanel.add(messageBox);
        libraryPanel.setComponentZOrder(messageBox, 0);
        setKeyInputEnabled(false);

        // Back button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(648, 54));
        buttonPanel.setBackground(main.UiStyle.PANEL);

        JButton backButton = new JButton("Back");
        backButton.setFont(object.SuperObject.pixelFont);
        backButton.setPreferredSize(new Dimension(140, 40));
        backButton.setBackground(main.UiStyle.PANEL); 
        backButton.setForeground(main.UiStyle.TEXT); 
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(main.UiStyle.TEXT, 3));
        backButton.addActionListener(e -> {
            gp.playSound(sound.SoundEffects.Cue.CLICK);
            gp.player.isPuzzleGameActive = false;
            dispose();
        });
        
        // Hover effect
    backButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            backButton.setBackground(main.UiStyle.ACCENT);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            backButton.setBackground(main.UiStyle.PANEL); 
        }
    });

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gp.player.isPuzzleGameActive = false;
            }
        });
    }
    private void moveBookLeft() {
        if (selectedBookIndex > 0) {
            swapBooks(selectedBookIndex, selectedBookIndex - 1);
            selectedBookIndex--;
        } else {
            swapBooks(0, totalBooks - 1);
            selectedBookIndex = totalBooks - 1;
        }
        updateBookPositions();
        updateUpArrowPosition();
        checkIfSorted(); 
    }

    private void moveBookRight() {
        if (selectedBookIndex < totalBooks - 1) {
            swapBooks(selectedBookIndex, selectedBookIndex + 1);
            selectedBookIndex++;
        } else {
            swapBooks(totalBooks - 1, 0);
            selectedBookIndex = 0;
        }
        updateBookPositions();
        updateUpArrowPosition();
        checkIfSorted(); 
    }


    private void swapBooks(int index1, int index2) {
        gp.playSound(sound.SoundEffects.Cue.BOOK);


        int[] tempSize = bookSizes[index1];
        bookSizes[index1] = bookSizes[index2];
        bookSizes[index2] = tempSize;

        JLabel tempLabel = bookLabels.get(index1);
        bookLabels.set(index1, bookLabels.get(index2));
        bookLabels.set(index2, tempLabel);

        updateBookPositions();
    }

    private void updateBookPositions() {
        shelfLabel.removeAll(); 
        int currentX = 9;

        for (int i = 0; i < totalBooks; i++) {
            int width = bookSizes[i][0];
            int height = bookSizes[i][1];
            JLabel bookLabel = bookLabels.get(i);

            bookLabel.setBounds(currentX, shelfLabel.getHeight() - height, width, height);
            shelfLabel.add(bookLabel);

            bookXPositions[i * 2] = currentX;
            bookXPositions[i * 2 + 1] = width;
            currentX += width;
        }

        shelfLabel.repaint();
    }
    private void updateUpArrowPosition() {
        if (uparrowLabel != null && bookXPositions != null) {
            int bookX = bookXPositions[selectedBookIndex * 2];
            int bookWidth = bookXPositions[selectedBookIndex * 2 + 1];
            int arrowWidth = uparrowLabel.getIcon().getIconWidth();
            int xCenter = 60 + bookX + (bookWidth - arrowWidth) / 2;
            uparrowLabel.setLocation(xCenter, upArrowBaseY);
            libraryPanel.repaint();
        }
    }

    private void checkIfSorted() {
        if (sortByWidth ? gp.player.isLibraryPuzzleSolved2 : gp.player.isLibraryPuzzleSolved1) return;
        boolean isSorted = true;

        for (int i = 0; i < bookSizes.length - 1; i++) {
            if (bookSizes[i][sortByWidth ? 0 : 1] > bookSizes[i + 1][sortByWidth ? 0 : 1]) {
                isSorted = false; 
                break;
            }
        }

        if (isSorted) {
            gp.playSound(sound.SoundEffects.Cue.PUZZLE);
            if (sortByWidth) gp.player.isLibraryPuzzleSolved2 = true;
            else gp.player.isLibraryPuzzleSolved1 = true;
            gp.player.sortedPuzzles++;
            System.out.println(gp.player.sortedPuzzles);

            PuzzleDialogs.showCongratulations(this);

            removeListeners();
        }
    }
    private void removeListeners() {
        inputEnabled = false;
        for (KeyListener listener : getKeyListeners()) {
            removeKeyListener(listener);
        }
        for (MouseListener listener : leftarrowLabel.getMouseListeners()) {
            leftarrowLabel.removeMouseListener(listener);
        }
        for (MouseListener listener : rightarrowLabel.getMouseListeners()) {
            rightarrowLabel.removeMouseListener(listener);
        }
    }

    private void setKeyInputEnabled(boolean enabled) {
        inputEnabled = enabled;
    }
}

