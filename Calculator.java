package menhguiza1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Calculator extends JFrame {

    private JTextField display;
    private StringBuilder input;
    private boolean resetDisplay;

    public Calculator() {
        input = new StringBuilder();
        resetDisplay = false;

        setTitle("Kalkulator");
        setSize(300, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Display panel
        display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("style",Font.BOLD,40));
        display.setPreferredSize(new Dimension(200,150));
        add(display, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));
        add(buttonPanel, BorderLayout.CENTER);
        String[] buttons = {
        	"²", "√", "C", "⌫",
            "7", "8", "9", "÷",
            "4", "5", "6", "×",
            "1", "2", "3", "-",
            ".", "0", "=", "+"   
        };
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }
        setVisible(true);
    }
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("C")) {
                input.setLength(0);
                display.setText("");
            } else if (command.equals("⌫")) {
                int length = input.length();
                if (length > 0) {
                    input.deleteCharAt(length - 1);
                    display.setText(input.toString());
                }
            } else if (command.equals("=")) {
                try {
                    String result = evaluateExpression(input.toString());
                    display.setText(String.valueOf(result));
                    input.setLength(0);
                    input.append(result);
                } catch (Exception ex) {
                    display.setText("Error");
                }
            } else if (command.equals("²")) {
                try {
                    double value = Double.parseDouble(input.toString());
                    double result = Math.pow(value, 2);
                    display.setText(String.valueOf(result));
                    input.setLength(0);
                    input.append(result);
                } catch (Exception ex) {
                    display.setText("power");
                }
            } else if (command.equals("√")) {
                try {
                    double value = Double.parseDouble(input.toString());
                    double result = Math.sqrt(value);
                    display.setText(String.valueOf(result));
                    input.setLength(0);
                    input.append(result);
                } catch (Exception ex) {
                    display.setText("sqrt");
                }
            } else {
                if (resetDisplay) {
                    input.setLength(0);
                    resetDisplay = false;
                }
                input.append(command);
                display.setText(input.toString());
            }
        }

        private String evaluateExpression(String expression) {
            try {
                expression = expression.replaceAll("\\^2", "**2");
                String rpn = infixToRpn(expression);
                return String.valueOf(evaluateRpn(rpn));
            } catch (Exception e) {
                return "xaou";
            }
        }

        private String infixToRpn(String expression) {
            StringBuilder output = new StringBuilder();
            Stack<Character> operators = new Stack<>();

            for (char token : expression.toCharArray()) {
                if (Character.isDigit(token) || token == '.') {
                    output.append(token);
                } else if (token == '+' || token == '-' || token == '×' || token == '÷') {
                    output.append(' ');
                    while (!operators.isEmpty() && precedence(token) <= precedence(operators.peek())) {
                        output.append(operators.pop()).append(' ');
                    }
                    operators.push(token);
                } else if (token == '(') {
                    operators.push(token);
                } else if (token == ')') {
                    output.append(' ');
                    while (!operators.isEmpty() && operators.peek() != '(') {
                        output.append(operators.pop()).append(' ');
                    }
                    operators.pop();
                }
            }

            while (!operators.isEmpty()) {
                output.append(' ').append(operators.pop());
            }

            return output.toString();
        }

        private int precedence(char operator) {
            switch (operator) {
                case '+':
                case '-':
                    return 1;
                case '×':
                case '÷':
                    return 2;
                default:
                    return -1;
            }
        }

        private double evaluateRpn(String rpn) {
            Stack<Double> stack = new Stack<>();
            for (String token : rpn.split(" ")) {
                if (token.isEmpty()) continue;
                if (Character.isDigit(token.charAt(0))) {
                    stack.push(Double.parseDouble(token));
                } else {
                    double b = stack.pop();
                    double a = stack.pop();
                    switch (token.charAt(0)) {
                        case '+':
                            stack.push(a + b);
                            break;
                        case '-':
                            stack.push(a - b);
                            break;
                        case '×':
                            stack.push(a * b);
                            break;
                        case '÷':
                            stack.push(a / b);
                            break;
                    }
                }
            }
            return stack.pop();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}

