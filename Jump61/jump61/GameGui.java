package jump61;

import javax.swing.Box;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;




/** the Gui for the jump61.
* @author Shin-Yen Huang
*/
public class GameGui extends Frame {
    /** constructor takes in @param game1 for Gui. */
    public GameGui(Game game1) {
        _game = game1;
        _board = _game.getBoard();
        _p1 = new HumanPlayer(_game, jump61.Color.RED);
        _p2 = new AI(_game, jump61.Color.BLUE);
        _playing = false;
        frame = new JFrame();
        bpanel = new JPanel();
        setAll();
        updateBoard();
        frame.setVisible(true);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
        rightPanInit();
        newGameList();
        exitList();
        startList();
        clearList();
        moveList();
        sizeList();
        seedList();
        mouseList();
    }


    /** init the right panel. */
    private void rightPanInit() {
        Box rightBox = Box.createVerticalBox();
        JPanel rightPanel = new JPanel();
        JPanel comboPanel1 = new JPanel();
        JPanel comboPanel2 = new JPanel();
        int dimA = Defaults.DIM_A;
        int dimB = Defaults.DIM_B;
        int dimC = Defaults.DIM_C;
        comboPanel1.setPreferredSize(new Dimension(dimA, Defaults.MENU_LEN));
        comboPanel2.setPreferredSize(new Dimension(dimA, Defaults.MENU_LEN));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        buttonSet();
        JLabel play1 = new JLabel();
        play1.setText("Red Player");
        JLabel play2 = new JLabel();
        play2.setText("Blue Player");
        String[] blueState = {"Auto", "Manual"};
        String[] redState = {"Manual", "Auto"};
        final JComboBox<String> redp = new JComboBox<String>(redState);
        final JComboBox<String> bluep = new JComboBox<String>(blueState);
        redp.setBackground(Color.white);
        bluep.setBackground(Color.white);
        redp.setPreferredSize(new Dimension(dimC, dimC / 3));
        bluep.setPreferredSize(new Dimension(dimC, dimC / 3));
        redp.setEditable(false);
        bluep.setEditable(false);
        rightBox.add(Box.createVerticalStrut(10));
        rightBox.add(Box.createVerticalGlue());
        rightBox.add(newGameb);
        rightBox.add(Box.createVerticalStrut(10));
        rightBox.add(startb);
        rightBox.add(Box.createVerticalStrut(10));
        rightBox.add(clearb);
        rightBox.add(Box.createVerticalStrut(10));
        rightBox.add(sizeb);
        rightBox.add(Box.createVerticalStrut(10));
        rightBox.add(moveb);
        rightBox.add(Box.createVerticalStrut(10));
        rightBox.add(seedb);
        rightBox.add(Box.createVerticalStrut(10));
        rightBox.add(exitb);
        rightBox.add(Box.createVerticalStrut(10));
        rightBox.add(play1);
        comboPanel1.add(redp);
        rightBox.add(comboPanel1);
        rightBox.add(Box.createVerticalStrut(10));
        rightBox.add(play2);
        comboPanel2.add(bluep);
        rightBox.add(comboPanel2);
        rightBox.add(Box.createVerticalGlue());
        rightBox.add(Box.createVerticalStrut(10));
        rightBox.setPreferredSize(new Dimension(dimB, dimB * 2));
        rightPanel.add(rightBox);
        frame.getContentPane().add(new JScrollPane(rightPanel),
            BorderLayout.EAST);
        frame.setVisible(true);
        setRedpList(redp);
        setBluepList(bluep);
    }

    /** sets buttons up. */
    private void buttonSet() {
        newGameb = new JButton("New Game");
        exitb = new JButton("Exit");
        clearb = new JButton("Clear");
        startb = new JButton("Start");
        sizeb = new JButton("Size");
        moveb = new JButton("Move");
        seedb = new JButton("Seed");
    }

    /** the listener for clicks. */
    private void mouseList() {
        frame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent add) {
                int r = ((add.getY())
                    / Defaults.PANEL_SIZE) + 1;
                int c = (add.getX() / Defaults.PANEL_SIZE) + 1;
                if (_board.whoseMove() == _p1.getColor()
                    && _p1.getClass().equals(HumanPlayer.class)
                    && _playing) {
                    if (_board.exists(r, c)
                        && _board.isLegal(_board.whoseMove(), r, c)) {
                        _board.saveAddSpot(_p1.getColor(), r, c, true);
                        statLabel.setText(String.format("Red moves %d %d."
                            , r, c));
                    }
                }
                if (_board.whoseMove() == _p2.getColor()
                    && _p2.getClass().equals(HumanPlayer.class)
                    && _playing) {
                    if (_board.exists(r, c)
                        && _board.isLegal(_board.whoseMove(), r, c)) {
                        _board.saveAddSpot(_p2.getColor(), r, c, true);
                        statLabel.setText(String.format("Blue moves %d %d."
                            , r, c));
                    }
                }
                updateBoard();
                run();
            }
        });
    }
    /** the red combo box with RED. */
    private void setRedpList(JComboBox<String> red) {
        final JComboBox<String> redp = red;
        redp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = (String) redp.getSelectedItem();
                switch (s) {
                case "Auto":
                    _p1 = new AI(_game, jump61.Color.RED);
                    statLabel.setText("Please start the game");
                    _playing = false;
                    updateBoard();
                    break;
                case "Manual":
                    _p1 = new HumanPlayer(_game, jump61.Color.RED);
                    statLabel.setText("Please start the game");
                    _playing = false;
                    updateBoard();
                    break;
                default:
                    break;
                }
            }
        });
    }
    /** the BLUE combo box. */
    private void setBluepList(JComboBox<String> blue) {
        final JComboBox<String> bluep = blue;
        bluep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = (String) bluep.getSelectedItem();
                switch (s) {
                case "Auto":
                    _p1 = new AI(_game, jump61.Color.RED);
                    statLabel.setText("Please start the game");
                    _playing = false;
                    updateBoard();
                    break;
                case "Manual":
                    _p1 = new HumanPlayer(_game, jump61.Color.RED);
                    statLabel.setText("Please start the game");
                    _playing = false;
                    updateBoard();
                    break;
                default:
                    break;
                }
            }
        });
    }

    /** Listener for the seed. */
    private void seedList() {
        seedb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                while (true) {
                    String seed = JOptionPane.showInputDialog(frame,
                        "Input Seed", null);
                    if (seed == null) {
                        break;
                    }
                    try {
                        long n = Long.parseLong(seed);
                        if (n < 0) {
                            continue;
                        }
                        _game.setSeed(n);
                        break;
                    } catch (NumberFormatException err) {
                        continue;
                    }
                }
            }
        });
    }
    /** listerner for size icon. */
    private void sizeList() {
        sizeb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                while (true) {
                    String size = JOptionPane.showInputDialog(frame,
                        "Input Size", null);
                    if (size == null) {
                        break;
                    }
                    try {
                        int n = Integer.parseInt(size);
                        if (n < 0) {
                            continue;
                        }
                        _board.clear(n);
                        break;
                    } catch (NumberFormatException err) {
                        continue;
                    }
                }
                statLabel.setText("Please start the game");
                updateBoard();
                _playing = false;
            }
        });
    }
    /** listener for the move icon. */
    private void moveList() {
        moveb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                while (true) {
                    String move = JOptionPane.showInputDialog(frame,
                        "Input Move Number", null);
                    if (move == null) {
                        break;
                    }
                    try {
                        int n = Integer.parseInt(move);
                        if (n < 0) {
                            continue;
                        }
                        _board.setMoves(n);
                        break;
                    } catch (NumberFormatException err) {
                        continue;
                    }
                }
                statLabel.setText("Please start the game");
                updateBoard();
                _playing = false;
            }
        });
    }
    /** calls the clear method and clears the board. */
    private void clearList() {
        clearb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _board.clear(_board.size());
                statLabel.setText("Please start the game");
                updateBoard();
                _playing = false;
            }
        });
    }
    /** starts the game. */
    private void startList() {
        startb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _playing = true;
                statLabel.setText("Please make a Move");
                run();
            }
        });
    }
    /** exits the game. */
    private void exitList() {
        exitb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    /** starts a new game. */
    private void newGameList() {
        newGameb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _board.clear(_board.size());
                updateBoard();
                _playing = true;
                statLabel.setText("Please start the game");
            }
        });
    }
    /** inits menu and fram and game. */
    private void setAll() {
        setFrame();
        setStat();
    }
    /** inits Stats bar. */
    private void setStat() {
        statBar = new JPanel();
        statBar.setBorder(BorderFactory.createLoweredBevelBorder());
        frame.add(statBar, BorderLayout.SOUTH);
        statBar.setPreferredSize(new Dimension(frame.getWidth(),
            Defaults.STATUS_LEN));
        statBar.setLayout(new BoxLayout(statBar, BoxLayout.X_AXIS));
        statLabel = new JLabel("Please start the game");
        statLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statBar.add(statLabel);
    }
    /** sets the frame. */
    private void setFrame() {
        frame.setTitle("jump61");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Defaults.FRAME_WIDTH, Defaults.FRAME_LEN
            + Defaults.STATUS_LEN);
        bpanel.setSize(Defaults.FRAME_WIDTH, Defaults.FRAME_LEN);
        frame.setBackground(Color.white);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
    }
    /** runs the AI. */
    public void run() {
        if (_board.getWinner() != null) {
            _playing = false;
        } else {
            if (_board.whoseMove() == _p1.getColor()
                && _p1.getClass().equals(AI.class)
                && _playing) {
                _p1.makeMove();
                statLabel.setText(String.format("Red moves %d %d.",
                    _p1.getArr()[0],
                    _p1.getArr()[1]));
            } else if (_board.whoseMove() == _p2.getColor()
                && _p2.getClass().equals(AI.class)
                && _playing) {
                _p2.makeMove();
                statLabel.setText(String.format("Blue moves %d %d.",
                    _p2.getArr()[0],
                    _p2.getArr()[1]));
            }
            if (_p2.getClass().equals(AI.class)
                && (_p1.getClass().equals(AI.class))
                && _playing) {
                updateBoard();
                run();
            } else {
                updateBoard();
            }
        }
    }
    /** updates the board each turn. */
    public void updateBoard() {
        if (_playing && _board.getWinner() != null) {
            JOptionPane.showMessageDialog(frame, String.format("%s wins.",
                _board.getWinner().toCapitalizedString()));
            statLabel.setText(String.format("Please start the game."));
            _playing = false;
        }
        int panSize = Defaults.PANEL_SIZE;
        int frameWidth = panSize * _board.size();
        int frameLen = panSize * _board.size();
        frame.setPreferredSize(new Dimension(frameWidth, frameLen
            + Defaults.STATUS_LEN));
        bpanel.removeAll();
        bpanel.setLayout(new GridLayout(_board.size(), _board.size()));
        for (int x = 1; x <= _board.size() * _board.size(); x += 1) {
            final int panelspot = _board.spots(x - 1);
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics grphcs) {
                    super.paintComponent(grphcs);
                    int spots = panelspot;
                    g = (Graphics2D) grphcs;
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setColor(Color.BLACK);
                    int size = Defaults.CIRC_SIZE;
                    int top = Defaults.TOP_DIE;
                    int right = Defaults.RIGHT_DIE;
                    int mid = Defaults.MID_DIE;
                    if (spots == 1) {
                        g.fillOval(mid, mid, size, size);
                    } else if (spots == 3) {
                        g.fillOval(top, top, size, size);
                        g.fillOval(mid, mid, size, size);
                        g.fillOval(right, right, size, size);
                    } else if (spots == 4) {
                        g.fillOval(right, top, size, size);
                        g.fillOval(top, right, size, size);
                        g.fillOval(top, top, size, size);
                        g.fillOval(right, right, size, size);
                    } else if (spots == 2) {
                        g.fillOval(top, top, size, size);
                        g.fillOval(right, right, size, size);
                    }
                }
            };
            panel.setPreferredSize(new Dimension(panSize, panSize));
            panel.setBorder(BorderFactory.createLineBorder(Color.black));
            if (_board.color(x - 1) == jump61.Color.RED) {
                panel.setBackground(Color.red);
            } else if (_board.color(x - 1) == jump61.Color.BLUE) {
                panel.setBackground(Color.blue);
            }
            bpanel.add(panel);
        }
        frame.add(bpanel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
        frame.pack();
    }

    /** the panel rep of the _board. */
    private JPanel bpanel;
    /** the fram for GUI. */
    private JFrame frame;
    /** menu for GUI. */
    private JMenuBar menu;
    /** status bar. */
    private JPanel statBar;
    /** Label for the status. */
    private JLabel statLabel;
    /** graphics for gui. */
    private Graphics2D g;
    /** file menu items. */
    private JMenu file, game, player;
    /** file tab options. */
    private JButton newGameb, clearb, exitb;
    /** game tab options. */
    private JButton startb, moveb, seedb, sizeb;
    /** player tabs for player bard. */
    private JMenu _p1M, _p2M;
    /** auto manual actions for _p1,2. */
    private JMenuItem autor, manualr, manualb, autob;
    /** board for the game. */
    private Board _board;
    /** game for GUI. */
    private Game _game;
    /** players for the game. */
    private Player _p1, _p2;
    /** boolean for whether or not game is playing. */
    private boolean _playing = false;
}
