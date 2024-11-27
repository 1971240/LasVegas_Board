package pages;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CasinoUI extends JFrame {

    public CasinoUI() {
        // 메인 프레임 설정
        setTitle("Casino UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // 카지노 패널 (6개의 구역)
        JPanel casinoPanel = new JPanel(new GridLayout(2, 3));
        for (int i = 1; i <= 6; i++) {
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createTitledBorder(i + "번 카지노"));

            // 이미지 추가
            JLabel imageLabel = new JLabel();
            String imagePath = "src/images/board" + i + ".png"; // 이미지 파일 경로
            File imageFile = new File(imagePath);

            if (imageFile.exists()) { // 이미지 파일이 존재하는지 확인
                ImageIcon imageIcon = new ImageIcon(imagePath);
                // 이미지 크기를 조정하여 JLabel에 맞게 표시
                Image scaledImage = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                imageLabel.setText("이미지 없음");
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }

            // 패널에 이미지 추가
            panel.add(imageLabel);
            casinoPanel.add(panel);
        }

        // 오른쪽 정보 및 제어 패널
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());

        // 유저 정보
        JPanel userInfoPanel = new JPanel(new GridLayout(3, 1));
        userInfoPanel.add(new JLabel("user"));
        userInfoPanel.add(new JLabel("money"));
        userInfoPanel.add(new JLabel("최종우승자"));
        infoPanel.add(userInfoPanel, BorderLayout.NORTH);

        // 주사위 롤링 섹션
        JPanel dicePanel = new JPanel(new GridLayout(2, 2));
        dicePanel.setBorder(BorderFactory.createTitledBorder("주사위"));
        for (int i = 0; i < 4; i++) {
            dicePanel.add(new JLabel(new ImageIcon("src/images/dice_placeholder.png"))); // 주사위 이미지
        }
        infoPanel.add(dicePanel, BorderLayout.CENTER);

        // 메시지 로그 및 이름 입력
        JPanel logPanel = new JPanel(new BorderLayout());
        JTextArea messageLog = new JTextArea("메시지 로그\n이름을 입력하시고 '바로시작' 버튼을 클릭하세요.");
        messageLog.setEditable(false);
        logPanel.add(new JScrollPane(messageLog), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField nameInput = new JTextField();
        inputPanel.add(nameInput, BorderLayout.CENTER);
        JButton startButton = new JButton("바로시작");
        JButton rollButton = new JButton("Roll");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(rollButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        logPanel.add(inputPanel, BorderLayout.SOUTH);
        infoPanel.add(logPanel, BorderLayout.SOUTH);

        // 메인 레이아웃 구성
        add(casinoPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);

        // 프레임 표시
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CasinoUI::new);
    }
}
