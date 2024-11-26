package org.example.service;
import java.util.*;

import org.example.model.*;
import org.example.repo.IRepository;

public class ExamService {
    private final IRepository<Exam> examRepo;

    private final IRepository<Student> studentRepo;

    private final IRepository<Teacher> teacherRepo;

    public ExamService(IRepository<Exam> examRepo, IRepository<Student> studentRepo, IRepository<Teacher> teacherRepo) {
        this.examRepo = examRepo;
        this.studentRepo = studentRepo;
        this.teacherRepo=teacherRepo;
    }

    public Student getStudentById(Integer studentId){
        for (Student student : studentRepo.getAll()) {
            if (student.getId().equals(studentId))
                return student;
        }
        return null;
    }

    public Teacher getTeacherById(Integer teacherId){
        for (Teacher teacher : teacherRepo.getAll()) {
            if (teacher.getId().equals(teacherId))
                return teacher;
        }
        return null;
    }

    public Exam getExamById(Integer examId){
        for (Exam exam : examRepo.getAll()) {
            if (exam.getId().equals(examId))
                return exam;
        }
        return null;
    }

    public void takeReadingExam(Integer studentId, Integer examId){
        Student student=getStudentById(studentId);

        Exam exam=getExamById(examId);
        String[][] exercises=exam.getExercises();
        Scanner scanner = new Scanner(System.in);
        String[] exercise;
        int foundCourse=0;
        float score=2;

        for (Course course:student.getCourses())
            if (course.getCourseName().contains("Reading"))
            {
                foundCourse=1;
                break;
            }

        if (foundCourse==1)
        {
            System.out.println(exercises[0][0]);
            System.out.println(exercises[1][0]);
            for (int i=2;i<6;i++)
            {
                exercise=exercises[i];
                System.out.println(exercise[0]+exercise[1]+"\n"+exercise[2]);
                System.out.println("Your answer: ");
                char answer = scanner.nextLine().charAt(0);
                int found=0;

                if (answer=='a' || answer=='b')
                {
                    for (int j=1;j<=2;j++)
                    {
                        if (exercise[j].charAt(0)==answer && exercise[j].charAt(1)=='.')
                            if (exercise[j] == exercise[3])
                            {
                                System.out.println("Correct! " + exercise[3]);
                                score+=2;
                                found=1;
                                break;
                            }
                    }
                    if (found==0)
                    {
                        System.out.println("Wrong! The right answer was " + exercise[3]);
                    }
                }
                else
                    System.out.println("Invalid choice!");
            }

            System.out.println("\n\n\nExam complete!\n\n\n");
            System.out.println("Your score: "+ score + "\n\n");
            Map<Integer, Float> readingExamResults=new HashMap<>();
            readingExamResults=student.getReadingResults();
            readingExamResults.put(examId,score);
            student.setReadingResults(readingExamResults);
            studentRepo.update(student);

            List<Student> examined;
            examined=exam.getExaminedStudents();
            examined.add(student);
            exam.setExaminedStudents(examined);
            examRepo.update(exam);

            Map<Student,Float> results;
            results=exam.getResults();
            results.put(student,score);
            exam.setResults(results);
            examRepo.update(exam);
            return;
        }

        if (foundCourse==0)
            System.out.println("\n\n\nYou are not enrolled in any reading course!");
    }

    public void showReadingResults(Integer studentId){
        for (Student stud:studentRepo.getAll())
            if (stud.getId().equals(studentId)){
                Map<Integer, Float> readingExamResults=new HashMap<>();
                readingExamResults=stud.getReadingResults();
                System.out.println("Your past scores: ");
                for (Map.Entry<Integer, Float> entry : readingExamResults.entrySet()) {
                    System.out.println("Reading exam id: " + entry.getKey() + ", Score: " + entry.getValue());
                }
            }
    }

    public void takeGrammarExam(Integer studentId, Integer examId){

        Student student=getStudentById(studentId);

        Exam exam=getExamById(examId);
        String []exercise;
        String[][] exercises=exam.getExercises();
        Scanner scanner = new Scanner(System.in);
        int correctAnswers=0;

        int foundCourse=0;
        for (Course findCourse : student.getCourses()){
            if (findCourse.getCourseName().contains("Grammar"))
            {
                foundCourse=1;
                break;}
        }
        if (foundCourse==0){
            System.out.println("\n\n\nYou are not enrolled in any grammar course!");}
        if(foundCourse==1){
            System.out.println("PLease fill in the gaps with the correct word:");
            for(int i=0; i<10; i++){
                exercise=exercises[i];
                System.out.println("Question "+i+": "+exercise[0]);
                System.out.print("Answer: ");
                String answer=scanner.nextLine();
                if(answer.equals(exercise[1]))
                    correctAnswers++;
            }
            if(correctAnswers>5) System.out.println("You have passed this exam with the grade "+correctAnswers+"!");
            else System.out.println("You have failed this exam with the grade "+correctAnswers);
            float grade=(float) correctAnswers;
            Map<Integer, Float> grammarExamResults=new HashMap<>();
            grammarExamResults=student.getGrammarResults();
            grammarExamResults.put(examId,grade);
            student.setGrammarResults(grammarExamResults);
            studentRepo.update(student);

            List<Student> examined;
            examined=exam.getExaminedStudents();
            examined.add(student);
            exam.setExaminedStudents(examined);
            examRepo.update(exam);

            Map<Student,Float> results;
            results=exam.getResults();
            results.put(student,grade);
            exam.setResults(results);
            examRepo.update(exam);
        }
    }

    public void showGrammarResults(Integer studentId){

        Student student=getStudentById(studentId);
        Map<Integer, Float> grammarExamResults=new HashMap<>();
        grammarExamResults=student.getGrammarResults();
        System.out.println("Your past scores: ");
        for (Map.Entry<Integer, Float> entry : grammarExamResults.entrySet()) {
            System.out.println("Grammar exam id: " + entry.getKey() + ", Score: " + entry.getValue());
        }
    }

    public void takeVocabExam(Integer studentId, Integer examId){

        Student student=getStudentById(studentId);

        Exam exam=getExamById(examId);

        Scanner scanner = new Scanner(System.in);
        int correctAnswers=0;
        int foundCourse=0;
        Map <Integer, Float> tempother=new HashMap<>();
        for (Course findCourse : student.getCourses()){
            if (findCourse.getCourseName().contains("Vocabulary"))
            {
                foundCourse=1;
                break;}
        }
        if (foundCourse==0){
            System.out.println("\n\n\nYou are not enrolled in this course!");}
        if(foundCourse==1){
            System.out.println("PLease write the correct translation for evey word (capital letter if needed):");
            String placeholderKey=new String();
            String placeholderValue=new String();
            for(int i=0; i<10; i++) {
                List<String> values = new ArrayList<>(exam.getWorter().values());
                Random random = new Random();
                int randomIndex = random.nextInt(values.size());
                String ubung = values.get(randomIndex);
                System.out.println(ubung+": ");
                String answer = scanner.nextLine();
                boolean found = false;
                for (Map.Entry<String, String> entry : exam.getWorter().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(ubung) && key.equals(answer)) {
                        System.out.println("Correct!");
                        found = true;
                    } else {
                        placeholderKey = key;
                        placeholderKey = value;
                    }
                }
                if (found == true)
                    correctAnswers++;
            }
            if(correctAnswers>5) System.out.println("You have passed this practice test with the grade "+correctAnswers+"!");
            else System.out.println("You have failed this practice test with the grade "+correctAnswers);
            tempother=student.getVocabResults();
            tempother.put(exam.getId(),(float)correctAnswers);
            student.setVocabResults(tempother);
            studentRepo.update(student);

            List<Student> examined;
            examined=exam.getExaminedStudents();
            examined.add(student);
            exam.setExaminedStudents(examined);
            examRepo.update(exam);

            Map<Student,Float> results;
            results=exam.getResults();
            results.put(student,(float)correctAnswers);
            exam.setResults(results);
            examRepo.update(exam);
        }

    }

    public void showVocabResults(Integer studentId){

        Student student=getStudentById(studentId);
        Map<Integer, Float> vocabExamResults=new HashMap<>();
        vocabExamResults=student.getVocabResults();
        System.out.println("Your past scores: ");
        for (Map.Entry<Integer, Float> entry : vocabExamResults.entrySet()) {
            System.out.println("Vocabulary exam id: " + entry.getKey() + ", Score: " + entry.getValue());
        }
    }


    public void takeWritingExam(Integer studentId, Integer examId){

        Student student=getStudentById(studentId);

        Exam exam=getExamById(examId);
        Teacher teacher=exam.getTeacher();
        Scanner scanner = new Scanner(System.in);
        StringBuilder answer = new StringBuilder();

        int foundCourse=0;

        for (Course findCourse : student.getCourses()){
            if (findCourse.getCourseName().contains("Writing"))
            {
                foundCourse=1;
                break;}
        }
        if (foundCourse==0){
            System.out.println("\n\n\nYou are not enrolled in this course!");}
        if(foundCourse==1) {
            String exercise=exam.getRequirement();
            System.out.println(exercise);
            String input;


            System.out.println("Enter text (type 0 to stop):");
            while (true) {
                input = scanner.nextLine();
                if (input.equals("0")) {
                    System.out.println("Exiting...");
                    break;
                }
                answer.append(input).append("\n");
            }
            Map <Student, String> toBeGraded=teacher.getGradeWriting();
            toBeGraded.put(student, answer.toString());
            teacher.setGradeWriting(toBeGraded);
            teacherRepo.update(teacher);
            System.out.println("Writing exam submitted!!!!! Waiting for the grade");
            List<Student> examined;
            examined=exam.getExaminedStudents();
            examined.add(student);
            exam.setExaminedStudents(examined);
            examRepo.update(exam);
        }
    }

    public void showWritingResults(Integer studentId){

        Student student=getStudentById(studentId);
        Map<Integer, Float> writingExamResults=new HashMap<>();
        writingExamResults=student.getWritingExamResults();
        System.out.println("Your past scores: ");
        for (Map.Entry<Integer, Float> entry : writingExamResults.entrySet()) {
            System.out.println("Writing exam id: " + entry.getKey() + ", Score: " + entry.getValue());
        }
    }

    public void gradeWritings(Integer teacherId, Integer examId){

        Exam exam=getExamById(examId);
        Teacher teacher=getTeacherById(teacherId);
        Scanner scanner=new Scanner(System.in);
        Map<Student, String> toGrade=teacher.getGradeWriting();
        while (!toGrade.isEmpty()) {
            Map.Entry<Student, String> entry = toGrade.entrySet().iterator().next();
            Student key = entry.getKey();
            String value = entry.getValue();
            System.out.println(value);
            System.out.println("Input grade: ");
            float grade=scanner.nextFloat();
            Map<Integer, Float> results=key.getWritingExamResults();
            results.put(examId, grade);
            key.setWritingExamResults(results);
            studentRepo.update(key);
            toGrade.remove(key);
        }

    }

    public void showAllReadingExams(){
        List<Exam> exams=examRepo.getAll();
        for (Exam exam:exams)
            if(exam.getExamName().contains("Reading"))
                System.out.println(exam);
    }


    public void showResultsOfAllStudentsOnReadingExam(Integer teacherId){
        for (Exam exam:examRepo.getAll()){
            if (exam.getExamName().contains("Reading"))
                if(exam.getTeacher().getId().equals(teacherId))
                    for (Student student:studentRepo.getAll())
                        for (Integer key : student.getReadingResults().keySet()) {
                            if(key.equals(exam.getId()))
                                System.out.println(student+" "+exam.getExamName()+" "+student.getReadingResults().get(key));
                        }
        }
    }

    public void removeReadingExam(Integer teacherId, Integer examId) {

        Exam exam=getExamById(examId);
        if (exam.getTeacher().getId().equals(teacherId)){
            examRepo.delete(examId);
        }
        else{
            System.out.println("You don't have access to this exam!");
        }
    }

    public void createOrUpdateReadingExam(Integer examId, Integer teacherId, String examName){
        int found=0;
        for (Exam exam: examRepo.getAll()){
            if (exam.getId().equals(examId))
            {
                found=1;
                updateReadingExam(examId,teacherId,examName);
                return;
            }
        }
        if (found==0){
            createReadingExam(examId,teacherId,examName);
        }
    }

    public void createReadingExam(Integer examId, Integer teacherId,String examName){

        Teacher teacher=getTeacherById(teacherId);
        Exam e1=new Exam(examId,examName,teacher);
        examRepo.create(e1);
        String[][] readingExercises = {
                {"Der Aufbruch\n" + "Franz Kafka","","",""},
                {"Ich befahl mein Pferd aus dem Stall zu holen. Der Diener verstand mich nicht.\nIch ging selbst in den Stall, sattelte mein Pferd und bestieg es. In der Ferne hörte ich eine Trompete blasen,\nich fragte ihn, was das bedeute. Er wusste nichts und hatte nichts gehört. Beim Tore hielt er mich auf und fragte:\n\"Wohin reitest du, Herr?\" \"Ich weiß es nicht,\" sagte ich, \"nur weg von hier. Immerfort weg von hier, nur so kann ich\nmein Ziel erreichen.\" \"Du kennst also dein Ziel?\" fragte er. \"Ja,\" antwortete ich, \"ich sagte es doch: »Weg-von-hier«,\ndas ist mein Ziel.\" \"Du hast keinen Essvorrat mit,\" sagte er. \"Ich brauche keinen,\" sagte ich, \"die Reise ist so lang,\ndass ich verhungern muss, wenn ich auf dem Weg nichts bekomme. Kein Essvorrat kann mich retten. Es ist ja zum Glück eine\nwahrhaft ungeheure Reise.\"", "", "", ""},
                {"\n\nDer Diener kann auf alle Fragen des Ich-Erzählers antworten.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                {"\n\nDer Ich-Erzähler nimmt einen Essvorrat.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                {"\n\nDer Ich-Erzähler unternimmt eine Reise, deren Dauer undefiniert ist.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                {"\n\nDie Parabel kann eine Metapher für das Unbekannte des Lebens darstellen.\n\n", "a. wahr", "b. falsch", "a. wahr"},
        };
        e1.setExercises(readingExercises);
        examRepo.update(e1);
    }

    public void updateReadingExam(Integer examId, Integer teacherId,String examName){

        Exam exam=getExamById(examId);
        Teacher teacher=getTeacherById(teacherId);
        examRepo.delete(examId);
        Exam e1=new Exam(examId,examName,teacher);
        examRepo.create(e1);
        String[][] readingExercises = {
                {"Der Aufbruch\n" + "Franz Kafka","","",""},
                {"Ich befahl mein Pferd aus dem Stall zu holen. Der Diener verstand mich nicht.\nIch ging selbst in den Stall, sattelte mein Pferd und bestieg es. In der Ferne hörte ich eine Trompete blasen,\nich fragte ihn, was das bedeute. Er wusste nichts und hatte nichts gehört. Beim Tore hielt er mich auf und fragte:\n\"Wohin reitest du, Herr?\" \"Ich weiß es nicht,\" sagte ich, \"nur weg von hier. Immerfort weg von hier, nur so kann ich\nmein Ziel erreichen.\" \"Du kennst also dein Ziel?\" fragte er. \"Ja,\" antwortete ich, \"ich sagte es doch: »Weg-von-hier«,\ndas ist mein Ziel.\" \"Du hast keinen Essvorrat mit,\" sagte er. \"Ich brauche keinen,\" sagte ich, \"die Reise ist so lang,\ndass ich verhungern muss, wenn ich auf dem Weg nichts bekomme. Kein Essvorrat kann mich retten. Es ist ja zum Glück eine\nwahrhaft ungeheure Reise.\"", "", "", ""},
                {"\n\nDer Diener kann auf alle Fragen des Ich-Erzählers antworten.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                {"\n\nDer Ich-Erzähler nimmt einen Essvorrat.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                {"\n\nDer Ich-Erzähler unternimmt eine Reise, deren Dauer undefiniert ist.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                {"\n\nDie Parabel kann eine Metapher für das Unbekannte des Lebens darstellen.\n\n", "a. wahr", "b. falsch", "a. wahr"},
        };
        e1.setExercises(readingExercises);
        examRepo.update(e1);
    }

    public void createOrUpdateWritingExam(Integer examId, Integer teacherId, String examName) {
        int found = 0;
        for (Exam exam : examRepo.getAll()) {
            if (exam.getId().equals(examId)) {
                found = 1;
                updateWritingExam(examId, teacherId, examName);
                return;
            }
        }
        if (found == 0) {
            createWritingExam(examId, teacherId, examName);
        }
    }

    public void createWritingExam(Integer examId, Integer teacherId, String examName) {

        Teacher teacher=getTeacherById(teacherId);
        Exam e1 = new Exam(examId, examName, teacher);
        examRepo.create(e1);
        String exercise="Schreibe einen Text über den Frühling. :3";
        e1.setRequirement(exercise);
        examRepo.update(e1);
    }

    public void updateWritingExam(Integer examId, Integer teacherId, String examName) {

        Exam exam=getExamById(examId);
        Teacher teacher=getTeacherById(teacherId);
        examRepo.delete(examId);
        Exam e1 = new Exam(examId, examName, teacher);
        examRepo.create(e1);
        String exercise="Schreibe einen Text über den Frühling. :3";
        e1.setRequirement(exercise);
        examRepo.update(e1);
    }

    public void removeWritingExam(Integer teacherId, Integer examId) {

        Exam exam=getExamById(examId);
        if (exam.getTeacher().getId().equals(teacherId)) {
            examRepo.delete(examId);
        } else {
            System.out.println("You don't have access to this exam!");
        }
    }


    public void showResultsOfAllStudentsOnWritingExam(Integer teacherId) {
        for (Exam exam : examRepo.getAll()) {
            if (exam.getExamName().contains("Writing")) {
                if (exam.getTeacher().getId().equals(teacherId)) {
                    for (Student student : studentRepo.getAll()) {
                        for (Integer key : student.getWritingExamResults().keySet()) {
                            if (key.equals(exam.getId())) {
                                System.out.println(
                                        student + " " + exam.getExamName() + ": " + student.getWritingExamResults().get(key)
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    public void createOrUpdateGrammarExam(Integer examId, Integer teacherId, String examName) {
        int found = 0;
        for (Exam exam : examRepo.getAll()) {
            if (exam.getId().equals(examId)) {
                found = 1;
                updateGrammarExam(examId, teacherId, examName);
                return;
            }
        }
        if (found == 0) {
            createGrammarExam(examId, teacherId, examName);
        }
    }

    public void createGrammarExam(Integer examId, Integer teacherId, String examName) {

        Teacher teacher=getTeacherById(teacherId);
        Exam e1 = new Exam(examId, examName, teacher);
        examRepo.create(e1);
        String [][] grammarExercises={
                { "Du (brauchen) _ Hilfe.", "brauchst" },
                { "Ich bin _ Hause.", "zu" },
                { "Er trägt _.", "bei" },
                { "Diana (setzen)_ sich auf das Sofa.", "setzt" },
                { "Stefi klettert auf _ Baum.", "den" },
                { "Ich (besuchen) _ diese Kirche.", "besuche" },
                { "Wir spielen DOTA in _ Klasse.", "der" },
                { "Mama kocht immer (lecker)_ Essen", "leckeres" },
                { "Der Ball ist unter _ Tisch gerollt.", "den" },
                { "Mein Mann kommt immer betrunken _ Hause.", "nach" }
        };
        e1.setExercises(grammarExercises);
        examRepo.update(e1);
    }

    public void updateGrammarExam(Integer examId, Integer teacherId, String examName) {

        Exam exam=getExamById(examId);

        Teacher teacher=getTeacherById(teacherId);
        examRepo.delete(examId);
        Exam e1 = new Exam(examId, examName, teacher);
        examRepo.create(e1);
        String [][] grammarExercises={
                { "Du (brauchen) _ Hilfe.", "brauchst" },
                { "Ich bin _ Hause.", "zu" },
                { "Er trägt _.", "bei" },
                { "Diana (setzen)_ sich auf das Sofa.", "setzt" },
                { "Stefi klettert auf _ Baum.", "den" },
                { "Ich (besuchen) _ diese Kirche.", "besuche" },
                { "Wir spielen DOTA in _ Klasse.", "der" },
                { "Mama kocht immer (lecker)_ Essen", "leckeres" },
                { "Der Ball ist unter _ Tisch gerollt.", "den" },
                { "Mein Mann kommt immer betrunken _ Hause.", "nach" }
        };
        e1.setExercises(grammarExercises);
        examRepo.update(e1);
    }

    public void removeGrammarExam(Integer teacherId, Integer examId) {

        Exam exam=getExamById(examId);
        if (exam.getTeacher().getId().equals(teacherId)) {
            examRepo.delete(examId);
        } else {
            System.out.println("You don't have access to this exam!");
        }
    }

    public void createOrUpdateVocabularyExam(Integer examId, Integer teacherId, String examName) {
        int found = 0;
        for (Exam exam : examRepo.getAll()) {
            if (exam.getId().equals(examId)) {
                found = 1;
                updateVocabularyExam(examId, teacherId, examName);
                return;
            }
        }
        if (found == 0) {
            createVocabularyExam(examId, teacherId, examName);
        }
    }

    public void createVocabularyExam(Integer examId, Integer teacherId, String examName) {

        Teacher teacher = getTeacherById(teacherId);
        Exam e1 = new Exam(examId, examName, teacher);
        examRepo.create(e1);
        Map<String, String> vocabularyExercises = new HashMap<>();
        vocabularyExercises.put("Hund", "dog");
        vocabularyExercises.put("Katze", "cat");
        vocabularyExercises.put("Apfel", "apple");
        vocabularyExercises.put("Buch", "book");
        vocabularyExercises.put("Haus", "house");
        vocabularyExercises.put("Auto", "car");
        vocabularyExercises.put("Baum", "tree");
        vocabularyExercises.put("Blume", "flower");
        vocabularyExercises.put("Fisch", "fish");
        vocabularyExercises.put("Brot", "bread");
        vocabularyExercises.put("Schule", "school");
        e1.setWorter(vocabularyExercises);
        examRepo.update(e1);
    }

    public void updateVocabularyExam(Integer examId, Integer teacherId, String examName) {

        Exam exam=getExamById(examId);

        Teacher teacher = getTeacherById(teacherId);
        examRepo.delete(examId);
        Exam e1 = new Exam(examId, examName, teacher);
        examRepo.create(e1);
        Map<String, String> vocabularyExercises = new HashMap<>();
        vocabularyExercises.put("Hund", "dog");
        vocabularyExercises.put("Katze", "cat");
        vocabularyExercises.put("Apfel", "apple");
        vocabularyExercises.put("Buch", "book");
        vocabularyExercises.put("Haus", "house");
        vocabularyExercises.put("Auto", "car");
        vocabularyExercises.put("Baum", "tree");
        vocabularyExercises.put("Blume", "flower");
        vocabularyExercises.put("Fisch", "fish");
        vocabularyExercises.put("Brot", "bread");
        vocabularyExercises.put("Schule", "school");
        e1.setWorter(vocabularyExercises);
        examRepo.update(e1);
    }

    public void removeVocabularyExam(Integer teacherId, Integer examId) {

        Exam exam=getExamById(examId);
        if (exam.getTeacher().getId().equals(teacherId)) {
            examRepo.delete(examId);
        } else {
            System.out.println("You don't have access to this exam!");
        }
    }

    public void showResultsOfAllStudentsOnGrammarExam(Integer teacherId) {
        for (Exam exam : examRepo.getAll()) {
            if (exam.getExamName().contains("Grammar")) {
                if (exam.getTeacher().getId().equals(teacherId)) {
                    for (Student student : studentRepo.getAll()) {
                        for (Integer key : student.getGrammarResults().keySet()) {
                            if (key.equals(exam.getId())) {
                                System.out.println(
                                        student + " " + exam.getExamName() + ": " + student.getGrammarResults().get(key)
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    public void showResultsOfAllStudentsOnVocabularyExam(Integer teacherId) {
        for (Exam exam : examRepo.getAll()) {
            if (exam.getExamName().contains("Vocabulary")) {
                if (exam.getTeacher().getId().equals(teacherId)) {
                    for (Student student : studentRepo.getAll()) {
                        for (Integer key : student.getVocabResults().keySet()) {
                            if (key.equals(exam.getId())) {
                                System.out.println(
                                        student + " " + exam.getExamName() + ": " + student.getVocabResults().get(key)
                                );
                            }
                        }
                    }
                }
            }
        }
    }

}
