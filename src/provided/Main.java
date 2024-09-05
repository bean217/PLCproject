package provided;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println();
        String testpath = String.format("%s/%s",System.getProperty("user.dir"), "tokenizerTestCases");
        // System.out.println(testpath);
        // File testfile = new File(String.format("%s", testpath));
        // File[] files = testfile.listFiles();
        // for (File file : files) {
        //     System.out.println(String.format("Running test: %s\n", file.getName()));
        //     var res = JottTokenizer.tokenize(String.format("%s/%s", testpath, file.getName()));
        //     if (res != null) {
        //         System.out.println("\tTokens:");
        //         for (Token token : res) {
        //             System.out.println(String.format("\t\t%s", token.getToken()));
        //         }
        //     }
        //     System.out.println();
        // }
        var res = JottTokenizer.tokenize(String.format("%s\\%s", testpath, "Phase1Example.jott"));
        if (res != null) {
            for (Token token : res) {
                System.out.println(String.format("%s\t%s", token.getToken(), token.getTokenType()));
            }
        }
    }
}
