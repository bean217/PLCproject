package provided;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        
        String testpath = String.format("%s\\%s",System.getProperty("user.dir"), "tokenizerTestCases");
        File testfile = new File(String.format("%s", testpath));
        File[] files = testfile.listFiles();
        for (File file : files) {
            System.out.println(String.format("Running test: %s\n", file.getName()));
            var res = JottTokenizer.tokenize(String.format("%s\\%s", testpath, file.getName()));
            if (res != null) {
                System.out.println("\tTokens:");
                for (Token token : res) {
                    System.out.println(String.format("\t\t%s", token.getToken()));
                }
            }
            System.out.println();
        }
        // var res = JottTokenizer.tokenize(path);
        // for (Token token : res) {
        //     System.out.println(token.getToken());
        // }
    }
}
