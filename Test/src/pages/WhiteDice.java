package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class WhiteDice extends JFrame {
    private JLabel[] diceLabels = new JLabel[8]; // 8개의 주사위를 위한 JLabel 배열
    private int[] diceNumbers = new int[8]; // 각 주사위의 숫자 저장
    private boolean[] selected = new boolean[8]; // 주사위 선택 여부 저장
    private Random random = new Random(); // 랜덤 객체

    public WhiteDice() {
        setTitle("라스베가스 주사위 게임");
        setSize(1200, 300); // 창 크기 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 창 배치

        // 메인 패널 (수평으로 주사위와 버튼 배치)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        // 주사위 라벨 추가
        for (int i = 0; i < 8; i++) {
            final int index = i;
            diceLabels[i] = new JLabel();
            diceLabels[i].setHorizontalAlignment(JLabel.CENTER);
            diceLabels[i].setPreferredSize(new Dimension(100, 100)); // 주사위 크기 설정
            setDiceImage(i, 1); // 초기 주사위 이미지를 1로 설정

            // 주사위 클릭 이벤트 추가
            diceLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleDiceClick(index); // 주사위 클릭 처리
                }
            });
            mainPanel.add(diceLabels[i]);

            if (i < 7) {
                mainPanel.add(Box.createHorizontalStrut(10)); // 주사위 간 간격
            }
        }

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createVerticalGlue()); // 버튼을 중앙 정렬

        JButton rollButton = new JButton("굴리기");
        rollButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollButton.addActionListener(e -> startDiceRolling()); // 굴리기 버튼 동작
        buttonPanel.add(rollButton);

        buttonPanel.add(Box.createVerticalStrut(10)); // 버튼 간 간격

        JButton rePlaceButton = new JButton("배치");
        rePlaceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(rePlaceButton);

        buttonPanel.add(Box.createVerticalStrut(10)); // 배치와 턴 종료 간 간격 추가

        JButton endTurnButton = new JButton("턴 종료");
        endTurnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(endTurnButton);

        buttonPanel.add(Box.createVerticalGlue());

        mainPanel.add(Box.createHorizontalStrut(5)); // 주사위와 버튼 간 간격
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    // 주사위 이미지를 설정하는 메서드
    private void setDiceImage(int diceIndex, int diceNumber) {
        // 클래스 경로에서 이미지 로드
        String imagePath = "/images/white_dice" + diceNumber + ".png";
        try {
            ImageIcon diceIcon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = diceIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            diceLabels[diceIndex].setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("이미지를 로드할 수 없습니다: " + imagePath);
        }
    }

    // 주사위를 굴리는 애니메이션 시작
    private void startDiceRolling() {
        for (int i = 0; i < 8; i++) {
            final int diceIndex = i;
            new Thread(() -> {
                try {
                    long rollingTime = 1000 + random.nextInt(1000); // 각 주사위가 1~2초 동안 굴러감
                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime < rollingTime) {
                        int diceNumber = random.nextInt(6) + 1; // 1 ~ 6 랜덤 숫자
                        setDiceImage(diceIndex, diceNumber);
                        Thread.sleep(100); // 100ms마다 이미지 변경
                    }
                    // 최종 주사위 숫자 설정
                    diceNumbers[diceIndex] = random.nextInt(6) + 1;
                    setDiceImage(diceIndex, diceNumbers[diceIndex]);
                    selected[diceIndex] = false; // 모든 선택 상태 초기화
                    diceLabels[diceIndex].setBorder(null); // 테두리 제거
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }

    // 주사위를 클릭했을 때 처리
    private void handleDiceClick(int index) {
        int clickedNumber = diceNumbers[index]; // 클릭한 주사위의 숫자
        for (int i = 0; i < 8; i++) {
            if (diceNumbers[i] == clickedNumber) {
                selected[i] = true; // 동일 숫자 주사위 선택
                diceLabels[i].setBorder(BorderFactory.createLineBorder(Color.RED, 2)); // 테두리 강조
            } else {
                selected[i] = false; // 다른 숫자 주사위 선택 해제
                diceLabels[i].setBorder(null); // 테두리 제거
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WhiteDice frame = new WhiteDice();
            frame.setVisible(true);
        });
    }
}
