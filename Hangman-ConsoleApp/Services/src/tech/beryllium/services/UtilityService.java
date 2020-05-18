package tech.beryllium.services;

public class UtilityService {
    /**
     * Iterates through a char array to determin it's content relative to a parameter
     * @param source the char array to be searched
     * @param target the target char
     * @return a boolean indicating weather or not the target is contained withing the array
     */
    public boolean charArrayContains(char[] source, char target) {
        for (var match : source) {

                if (match == target) {
                    return true;
                }

            }
        return false;
    }

    /**
     * A stupid and slow method to do what is built in to any decent modern programming language.
     * Replaces a char at an index of a string with another char
     * @param source the string with a replaceable char
     * @param index the index of the char to be replaced
     * @param replacement the char to be replaced with
     * @return the edited string
     */
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
