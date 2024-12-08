import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class MusicPlayerGUI extends JFrame {

    public static final Color FRAME_COLOR = new Color(26, 26, 26);
    public static final Color TEXT_COLOR = new Color(223, 179, 255);

    private MusicPlayer musicPlayer;
    private JFileChooser jFileChooser;
    private JLabel songTitle, songArtist;
    private JPanel playbackBtns;

    public MusicPlayerGUI() {
        super("Music Player");
        setSize(300, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        getContentPane().setBackground(FRAME_COLOR);

        musicPlayer = new MusicPlayer();

        jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("/home/kamikaze/Music"));
        jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));

        addGuiComponents();
    }

    private void addGuiComponents() {
        addToolbar();

        JLabel songImage = new JLabel(loadImage("src/assets/music.png", 100, 100));
        songImage.setBounds(0, 0, getWidth(), 230);
        add(songImage);

        songTitle = new JLabel("Song Title");
        songTitle.setBounds(0, 175, getWidth(), 30);
        songTitle.setFont(new Font("Mononoki Nerd Font Mono", Font.BOLD, 22));
        songTitle.setForeground(TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);

        songArtist = new JLabel("Artist");
        songArtist.setBounds(0, 205, getWidth(), 30);
        songArtist.setFont(new Font("Mononoki Nerd Font Mono", Font.PLAIN, 14));
        songArtist.setForeground(new Color(177, 77, 250));
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songArtist);

        JSlider playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setBounds(getWidth()/2 - 200/2, 255, 200, 40);
        playbackSlider.setBackground(null);
        add(playbackSlider);

        addPlaybackButtons();
    }

    private void addToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setBounds(0, 0, getWidth(), 24);
        toolBar.setFloatable(false);

        JMenuBar menuBar = new JMenuBar();
        toolBar.add(menuBar);

        JMenu songMenu = new JMenu("Song");
        JMenu playlistMenu = new JMenu("Playlist");
        menuBar.add(songMenu);
        menuBar.add(playlistMenu);

        JMenuItem loadSong = new JMenuItem("Load Song");
        loadSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = jFileChooser.showOpenDialog(MusicPlayerGUI.this);
                File selectFile = jFileChooser.getSelectedFile();
                if (result == JFileChooser.APPROVE_OPTION && selectFile != null) {
                    Song song = new Song(selectFile.getPath());
                    musicPlayer.loadSong(song);
                    updateSongTitleAndArtist(song);
                    enablePauseButtonDisablePlayButton();
                }
            }
        });
        JMenuItem createPlaylist = new JMenuItem("Create Playlist");
        JMenuItem loadPlaylist = new JMenuItem("Load Playlist");
        songMenu.add(loadSong);
        playlistMenu.add(createPlaylist);
        playlistMenu.add(loadPlaylist);

        add(toolBar);
    }

    private ImageIcon loadImage(String imagePath, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addPlaybackButtons() {
        playbackBtns = new JPanel();
        playbackBtns.setBounds(0, 325, getWidth(), 80);
        playbackBtns.setBackground(null);

        JButton prevButton = new JButton(loadImage("src/assets/previous.png", 25, 25));
        prevButton.setBorderPainted(false);
        prevButton.setBackground(null);
        playbackBtns.add(prevButton);

        JButton playButton = new JButton(loadImage("src/assets/play.png", 25, 25));
        playButton.setBorderPainted(false);
        playButton.setBackground(null);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enablePauseButtonDisablePlayButton();
                musicPlayer.playCurrentSong();
            }
        });
        playbackBtns.add(playButton);

        JButton pauseButton = new JButton(loadImage("src/assets/pause.png", 25, 25));
        pauseButton.setBorderPainted(false);
        pauseButton.setBackground(null);
        pauseButton.setVisible(false);
        pauseButton.setEnabled(false);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enablePlayButtonDisablePauseButton();
                musicPlayer.pauseSong();
            }
        });
        playbackBtns.add(pauseButton);

        JButton nextButton = new JButton(loadImage("src/assets/next.png", 25, 25));
        nextButton.setBorderPainted(false);
        nextButton.setBackground(null);
        playbackBtns.add(nextButton);

        add(playbackBtns);
    }

    private void updateSongTitleAndArtist(Song song) {
        songTitle.setText(song.getSongTitle());
        songArtist.setText(song.getSongArtist());
    }

    private void enablePauseButtonDisablePlayButton() {
        JButton playButton = (JButton) playbackBtns.getComponent(1);
        JButton pauseButton = (JButton) playbackBtns.getComponent(2);

        playButton.setVisible(false);
        playButton.setEnabled(false);
        pauseButton.setVisible(true);
        pauseButton.setEnabled(true);
    }

    private void enablePlayButtonDisablePauseButton() {
        JButton playButton = (JButton) playbackBtns.getComponent(1);
        JButton pauseButton = (JButton) playbackBtns.getComponent(2);

        playButton.setVisible(true);
        playButton.setEnabled(true);
        pauseButton.setVisible(false);
        pauseButton.setEnabled(false);
    }
}
