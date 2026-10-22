
public class LongStack {

   Node current = null;
   LongStack return_stack = new LongStack();

   public static void main(String[] argum) {
   }

   private class Node {
      long value;
      Node previous;
   }

   LongStack() {
      current = null;
   }

   @Override
   public Object clone() throws CloneNotSupportedException {
      LongStack output = new LongStack();

      copyNodes(current, output);

      return output;
   }

   private void copyNodes(Node node, LongStack output) {
      if (node == null) {
         return;
      }

      copyNodes(node.previous, output);
      output.push(node.value);
   }

   public boolean stEmpty() {
       return current == null;
   }

   public void push (long a) {
      Node oldNode = current;
      Node newNode = new Node();
      newNode.value = a;
      newNode.previous = oldNode;
      current = newNode;
   }

   public long pop() {
      long value = current.value;
      current = current.previous;
      return value;
   } // pop

   public void op (String s) {
      if (stEmpty()) {
         throw new RuntimeException("Stack underflow");
      }

      long right = pop();

      if (stEmpty()) {
         throw new RuntimeException("Stack underflow");
      }

      long left = pop();
      switch (s) {
         case "+":
            push(left + right);
            break;

         case "-":
            push(left - right);
            break;

         case "*":
            push(left * right);
            break;

         case "/":
            push(left / right);
            break;

         default:
            throw new RuntimeException("Illegal operator");
      }
   }
  
   public long tos() {
      if (stEmpty()) {
         throw new RuntimeException("Stack is empty");
      }
      return current.value;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }

      if (!(o instanceof LongStack)) {
         return false;
      }

      LongStack other = (LongStack) o;

      Node first = current;
      Node second = other.current;

      while (first != null && second != null) {
         if (first.value != second.value) {
            return false;
         }

         first = first.previous;
         second = second.previous;
      }

      return first == null && second == null;
   }

   @Override
   public String toString() {
      if (stEmpty()) {
         return "";
      }

      return toString(current);
   }

   private String toString(Node node) {
      if (node.previous == null) {
         return String.valueOf(node.value);
      }

      return toString(node.previous) + " " + node.value;
   }

   public static long interpret(String pol) {
      if (pol == null) {
         throw new RuntimeException("Invalid expression");
      }

      LongStack stack = new LongStack();

      String[] tokens = pol.trim().split("\\s+");

      for (String token : tokens) {
         if (token.equals("R<")) {
            stack.return_stack.push(stack.pop());
         }

         else if (token.equals("R>")) {
            stack.push(stack.return_stack.pop());
         }

         else if (token.equals("R@")) {
            stack.push(stack.return_stack.current.value);
         }

         else if (token.equals("+") ||
                 token.equals("-") ||
                 token.equals("*") ||
                 token.equals("/")) {

            stack.op(token);

         } else {
            try {
               long number = Long.parseLong(token);
               stack.push(number);
            } catch (NumberFormatException e) {
               throw new RuntimeException("Illegal symbol: " + token);
            }
         }
      }

      if (stack.stEmpty()) {
         throw new RuntimeException("No result");
      }

      long result = stack.pop();

      if (!stack.stEmpty()) {
         throw new RuntimeException("Redundant elements");
      }

      return result;
   }

}

