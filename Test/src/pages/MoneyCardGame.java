package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class MoneyCardGame extends JFrame {
    private JPanel[] zonePanels = new JPanel[6];
    private JPanel[] moneyCardPanels = new JPanel[6];
    private JLabel currentZoneLabel;
    private ArrayList<Integer> moneyCards;
    private int currentZone = 0;
    private int zoneSum = 0;
    private int cardYPosition = 10;

    public MoneyCardGame() {
        setTitle("Money Card 배치");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        moneyCards = initializeMoneyCards();
        setLayout(new BorderLayout());

        // 상단 - 카드 뽑기 버튼과 현재 구역 표시
        JPanel topPanel = new JPanel(new BorderLayout());
        currentZoneLabel = new JLabel("현재 구역: 1", JLabel.CENTER);
        topPanel.add(currentZoneLabel, BorderLayout.NORTH);

        String buttonImagePath = "/images/$Back.png";
        ImageIcon buttonIcon = new ImageIcon(getClass().getResource(buttonImagePath));
        Image scaledButtonImage = buttonIcon.getImage().getScaledInstance(100, 60, Image.SCALE_SMOOTH);
        JButton drawButton = new JButton(new ImageIcon(scaledButtonImage));
        drawButton.setPreferredSize(new Dimension(100, 60));
        drawButton.setContentAreaFilled(false);
        drawButton.setBorderPainted(false);
        drawButton.setFocusPainted(false);
        drawButton.addActionListener(new DrawCardListener());
        topPanel.add(drawButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // 중앙 - 6개의 구역 패널을 가로로 배치
        JPanel mainPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        for (int i = 0; i < 6; i++) {
            JPanel zonePanel = new JPanel(new GridBagLayout());
            zonePanel.setBorder(BorderFactory.createTitledBorder("구역 " + (i + 1)));
            zonePanels[i] = zonePanel;

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(5, 5, 5, 5);

            // 돈 카드 배치 패널 (첫 번째 칸 - 큰 비율로 설정)
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 0.5;  // 높이를 크게 설정
            JPanel moneyCardPanel = new JPanel(null);
            moneyCardPanel.setPreferredSize(new Dimension(100, 150));
            moneyCardPanels[i] = moneyCardPanel;
            zonePanel.add(moneyCardPanel, gbc);

            // 보드 이미지 패널 (두 번째 칸 - 작게 설정)
            gbc.gridy = 1;
            gbc.weighty = 0.3;  // 높이를 상대적으로 작게 설정
            String imagePath = "/images/board" + (i + 1) + ".png";
            CustomPanel boardImagePanel = new CustomPanel(imagePath);
            boardImagePanel.setPreferredSize(new Dimension(100, 80));
            zonePanel.add(boardImagePanel, gbc);

            // 주사위 배치 패널 (세 번째 칸 - 중간 크기로 설정)
            gbc.gridy = 2;
            gbc.weighty = 0.2;
            JPanel dicePanel = new JPanel();
            dicePanel.setPreferredSize(new Dimension(100, 80));
            dicePanel.setBackground(Color.LIGHT_GRAY);
            JLabel diceLabel = new JLabel("주사위 공간");
            dicePanel.add(diceLabel);
            zonePanel.add(dicePanel, gbc);

            mainPanel.add(zonePanel);
        }
        add(mainPanel, BorderLayout.CENTER);
    }

    private ArrayList<Integer> initializeMoneyCards() {
        ArrayList<Integer> cards = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            for (int j = 0; j < 5; j++) {
                cards.add(i * 10000);
            }
        }
        Collections.shuffle(cards);
        return cards;
    }

    private class CustomPanel extends JPanel {
        private Image backgroundImage;

        public CustomPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
            } catch (Exception e) {
                System.out.println("이미지 로드 실패: " + imagePath + " - " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private class DrawCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (moneyCards.isEmpty()) {
                JOptionPane.showMessageDialog(null, "모든 카드를 사용했습니다!");
                return;
            }

            if (currentZone >= 6) {
                JOptionPane.showMessageDialog(null, "게임을 시작하겠습니다!");
                return;
            }

            int drawnCard = moneyCards.remove(0);
            zoneSum += drawnCard;

            // 카드 이미지 생성 및 추가
            JLabel cardLabel = new JLabel();
            String cardImagePath = "/images/$" + drawnCard + ".png";
            ImageIcon cardIcon = new ImageIcon(getClass().getResource(cardImagePath));
            Image scaledCardImage = cardIcon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH);
            cardLabel.setIcon(new ImageIcon(scaledCardImage));

            cardLabel.setBounds(10, cardYPosition, 80, 100);
            moneyCardPanels[currentZone].add(cardLabel, 0);
            moneyCardPanels[currentZone].revalidate();
            moneyCardPanels[currentZone].repaint();
            cardYPosition += 20; // 새로 뽑은 카드가 기존 카드 아래에 포개지도록 배치

            if (zoneSum >= 50000) {
                currentZone++;
                zoneSum = 0;
                cardYPosition = 10;
                if (currentZone < 6) {
                    currentZoneLabel.setText("현재 구역: " + (currentZone + 1));
                } else {
                    currentZoneLabel.setText("모든 구역이 채워졌습니다!");
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MoneyCardGame frame = new MoneyCardGame();
            frame.setVisible(true);
        });
    }
}
