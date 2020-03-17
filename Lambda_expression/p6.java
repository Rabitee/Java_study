import java.util.function.ToIntFunction;

public class p6{
    private static Student[] students = {
        new Student("Tom", 90, 96),
        new Student("John", 95, 93)
    };
    public static double avg(ToIntFunction<Student> f) {
        double sum = 0;
        for(Student s : students) {
            sum += f.applyAsInt(s);
        }
        return sum / students.length;
    }
    public static void main(String[] args){
        // double englishAvg = avg(s -> s.getEnglishScore());
        double englishAvg = avg(Student :: getEnglishScore);
        System.out.println("Avg English score: " + englishAvg);
        // double mathAvg = avg(s -> s.getMathScore());
        double mathAvg = avg(Student :: getMathScore);
        System.out.println("Avg Math score: " + mathAvg);
    }

    public static class Student {
        private String name;
        private int englishScore;
        private int mathScore;

        public Student(String name, int englishScore, int mathScore) {
            this.name = name;
            this.englishScore = englishScore;
            this.mathScore = mathScore;
        }

        public String getName() { return name; }
        public int getEnglishScore() { return englishScore; }
        public int getMathScore() { return mathScore; }
    }
}