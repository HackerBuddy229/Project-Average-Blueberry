package tech.beryllium.services;

public class UtilityService {

    public boolean charArrayContains(char[] source, char target) {
        for (var match : source) {

                if (match == target) {
                    return true;
                }

            }
        return false;
    }

    public String replaceCharAtIndex(String source, int index, char replacement) {
        var str = new StringBuilder();

        for (int forIndex = 0; forIndex < source.length(); forIndex++) {
            if (forIndex == index) {
                str.append(replacement);
            } else {
                str.append(source.charAt(forIndex));
            }
        }
        return str.toString();
    }
}
