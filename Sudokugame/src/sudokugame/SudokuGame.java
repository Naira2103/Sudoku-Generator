package sudokugame;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.List;
import javax.swing.*;
public class SudokuGame implements ActionListener {
    private JFrame frame;
    private JPanel currentPanel;
    private SudokuPanel sudokuPanel;
    public static void main(String[] args) {
        SudokuGame game = new SudokuGame();
        game.run();
    }
    public void run() {
        frame = new JFrame("Sudoku Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentPanel = createWelcomePanel();
        frame.getContentPane().add(currentPanel);
        frame.pack();
        frame.setSize(500,500 );
        frame.setVisible(true);
    }
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Welcome to Sudoku Game");
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        JButton button = new JButton("Start");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(this);
        panel.add(button);
        return panel;
    }
    private JPanel createLevelPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Select level");
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setLayout(new GridLayout(3, 1));
        JButton easyButton = new JButton("Easy");
        easyButton.addActionListener(this);
        buttonPanel.add(easyButton);
        JButton mediumButton = new JButton("Medium");
        mediumButton.addActionListener(this);
        buttonPanel.add(mediumButton);
        JButton hardButton = new JButton("Hard");
        hardButton.addActionListener(this);
        buttonPanel.add(hardButton);
        panel.add(buttonPanel);
        return panel;
    }
    private JPanel createGamePanel(int[][] puzzle) {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    // Add timer to the top of the panel
    JLabel timerLabel = new JLabel("00:00");
    timerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
    timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(timerLabel, BorderLayout.NORTH);
    // Create Sudoku panel
    JPanel sudokuPanel = new SudokuPanel(puzzle);
    this.sudokuPanel = (SudokuPanel) sudokuPanel;
    panel.add(sudokuPanel, BorderLayout.CENTER);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    JButton submitButton = new JButton("Submit");
    submitButton.addActionListener(this);
    buttonPanel.add(submitButton);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    // Start the timer
    Timer timer = new Timer(1000, new ActionListener() {
        private int time = 0;
        public void actionPerformed(ActionEvent e) {
            time++;
            int minutes = time / 60;
            int seconds = time % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        }
    });
    timer.start();
    return panel;
}
    private JPanel createCongratulationsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Congratulations, you solved the puzzle!");
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(this);
        buttonPanel.add(newGameButton);
        JButton exitButton =     new JButton("Exit");
    exitButton.addActionListener(this);
    buttonPanel.add(exitButton);
    panel.add(buttonPanel);
    return panel;
}
public void actionPerformed(ActionEvent event) {
    String command = event.getActionCommand();
    if (command.equals("Start")) {
        currentPanel = createLevelPanel();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(currentPanel);
        frame.revalidate();
    } else if (command.equals("Easy")) {
        int[][] puzzle = SudokuGenerator.generate(20);
        currentPanel = createGamePanel(puzzle);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(currentPanel);
        frame.revalidate();
    } else if (command.equals("Medium")) {
        int[][] puzzle = SudokuGenerator.generate(40);
        currentPanel = createGamePanel(puzzle);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(currentPanel);
        frame.revalidate();
    } else if (command.equals("Hard")) {
        int[][] puzzle = SudokuGenerator.generate(60);
        currentPanel = createGamePanel(puzzle);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(currentPanel);
        frame.revalidate();
    } else if (command.equals("Submit")) {
        if (sudokuPanel.isSolved()) {
            currentPanel = createCongratulationsPanel();
            frame.getContentPane().removeAll();
            frame.getContentPane().add(currentPanel);
            frame.revalidate();
        } else {
            JOptionPane.showMessageDialog(frame, "The puzzle is not yet solved.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else if (command.equals("New Game")) {
        currentPanel = createLevelPanel();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(currentPanel);
        frame.revalidate();
    } else if (command.equals("Exit")) {
        System.exit(0);
    }
}
private static class SudokuGenerator {
    public static int[][] generate(int level) {
        int[][] puzzle = new int[9][9];
        generateHelper(puzzle, 0, 0);
        removeCells(puzzle, level);
        return puzzle;
    }
}
private static boolean generateHelper(int[][] puzzle, int row, int col) {
    if (row == 9) {
        row = 0;
        if (++col == 9) {
            return true;
        }
    }
    if (puzzle[row][col] != 0) {
        return generateHelper(puzzle, row + 1, col);
    }
    List<Integer> candidates = getCandidates(puzzle, row, col);
    for (int val : candidates) {
        puzzle[row][col] = val;
        if (generateHelper(puzzle, row + 1, col)) {
            return true;
        }
    }
    puzzle[row][col] = 0;
    return false;
}
private static List getCandidates(int[][] puzzle, int row, int col) {
    boolean[] used = new boolean[10];
    for (int i = 0; i < 9; i++) {
        used[puzzle[row][i]] = true;
        used[puzzle[i][col]] = true;
        used[puzzle[row - row % 3 + i / 3][col - col % 3 + i % 3]] = true;
    }
    List<Integer> candidates = new ArrayList<>();
    for (int i = 1; i <= 9; i++) {
        if (!used[i]) {
            candidates.add(i);
        }
    }
    Collections.shuffle(candidates);
    return candidates;
}
private static void removeCells(int[][] puzzle, int level) {
    Random rand = new Random();
    for (int i = 0; i < level; i++) {
        int row = rand.nextInt(9);
        int col = rand.nextInt(9);
        while (puzzle[row][col] == 0) {
            row = rand.nextInt(9);
            col = rand.nextInt(9);
        }
        int temp = puzzle[row][col];
        puzzle[row][col] = 0;
        if (!hasUniqueSolution(puzzle)) {
            puzzle[row][col] = temp;
        }
    }
}
private static boolean hasUniqueSolution(int[][] puzzle) {
    int[][] copy = new int[9][9];
    for (int i = 0; i < 9; i++) {
        System.arraycopy(puzzle[i], 0, copy[i], 0, 9);
    }
    return hasUniqueSolutionHelper(copy, 0, 0);
}
private static boolean hasUniqueSolutionHelper(int[][] puzzle, int row, int col) {
    if (row == 9) {
        row = 0;
        if (++col == 9) {
            return true;
        }
    }
    if (puzzle[row][col] != 0) {
        return hasUniqueSolutionHelper(puzzle, row + 1, col);
    }
    List<Integer> candidates = getCandidates(puzzle, row, col);
    for (int val : candidates) {
        puzzle[row][col] = val;
        if (hasUniqueSolutionHelper(puzzle, row + 1, col)) {
            return true;
        }
    }
    puzzle[row][col] = 0;
    return false;
}
private class SudokuPanel extends JPanel {
    private JTextField[][] grid;
    public SudokuPanel(int[][] puzzle) {
        super(new GridLayout(9, 9));
        grid = new JTextField[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField field = new JTextField();
                field.setHorizontalAlignment(JTextField.CENTER);
                field.setFont(new Font("SansSerif", Font.PLAIN, 24));
                if (puzzle[i][j] != 0) {
                    field.setText(Integer.toString(puzzle[i][j]));
                    field.setEditable(false);
                }
                grid[i][j] = field;
                add(field);
            }
        }
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setPreferredSize(new Dimension(450, 450));
    }
    public boolean isSolved() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String value = grid[row][col].getText();
                if (value.isEmpty() || !isValidValue(value, row, col)) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean isValidValue(String value, int row, int col) {
        // check if value is an integer
        try {
            int intValue = Integer.parseInt(value);
            if (intValue < 1 || intValue > 9) {
                // display error message
                JOptionPane.showMessageDialog(frame, "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            // display error message
            JOptionPane.showMessageDialog(frame, "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // check row
        for (int c = 0; c < 9; c++) {
            if (c != col && grid[row][c].getText().equals(value)) {
                // display error message
                JOptionPane.showMessageDialog(frame, "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        // check column
        for (int r = 0; r < 9; r++) {
            if (r != row && grid[r][col].getText().equals(value)) {
                // display error message
                JOptionPane.showMessageDialog(frame, "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        // check box
        int boxRow = row / 3 * 3;
        int boxCol = col / 3 * 3;
        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if ((r != row || c != col) && grid[r][c].getText().equals(value)) {
                    // display error message
                    JOptionPane.showMessageDialog(frame, "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }
}
}