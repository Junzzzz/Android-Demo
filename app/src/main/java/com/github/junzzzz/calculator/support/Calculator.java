package com.github.junzzzz.calculator.support;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

public class Calculator {
    public int calculate(String s) {
        return evalRPN(changeFix(s));
    }

    public String[] changeFix(String fixExpression) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfixExpression = new StringBuilder();

        for (int i = 0; i < fixExpression.length(); i++) {
            char c = fixExpression.charAt(i);
            char top;
            switch (c) {
                case ' ':
                    break;
                case '+':
                case '-':
                    while (!stack.isEmpty()) {
                        top = stack.pop();
                        if (top == '(') {
                            stack.push('(');
                            break;
                        }
                        postfixExpression.append(" ").append(top);
                    }
                    stack.push(c);
                    postfixExpression.append(" ");
                    break;
                case '*':
                case '/':
                    while (!stack.isEmpty()) {
                        top = stack.pop();
                        if (top == '(' || top == '+' || top == '-') {
                            stack.push(top);
                            break;
                        } else
                            postfixExpression.append(" ").append(top);
                    }
                    stack.push(c);
                    postfixExpression.append(" ");
                    break;
                case '(':
                    stack.push(c);
                    break;
                case ')':
                    while (!stack.isEmpty()) {
                        top = stack.pop();
                        if (top == '(')
                            break;
                        else
                            postfixExpression.append(" ").append(top);
                    }
                    break;
                default:
                    postfixExpression.append(c);
            }
        }
        while (!stack.isEmpty()) {
            postfixExpression.append(" ").append(stack.pop());
        }

        return postfixExpression.toString().split(" ");
    }

    public int evalRPN(String[] tokens) {
        Deque<Integer> stack = new LinkedList<>();
        int n = tokens.length;
        for (String token : tokens) {
            if (isNumber(token)) {
                stack.push(Integer.parseInt(token));
            } else {
                int num2 = stack.pop();
                int num1 = stack.pop();
                switch (token) {
                    case "+":
                        stack.push(num1 + num2);
                        break;
                    case "-":
                        stack.push(num1 - num2);
                        break;
                    case "*":
                        stack.push(num1 * num2);
                        break;
                    case "/":
                        stack.push(num1 / num2);
                        break;
                    default:
                }
            }
        }
        return stack.pop();
    }

    public boolean isNumber(String token) {
        return !("+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token));
    }
}
