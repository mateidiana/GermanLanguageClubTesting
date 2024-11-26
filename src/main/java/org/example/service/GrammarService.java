package org.example.service;
import java.util.Scanner;
import java.util.List;

import org.example.model.*;
import org.example.repo.IRepository;
import org.example.model.Student;

public class GrammarService {
    private final IRepository<Grammar> grammarRepo;
    private final IRepository<Student> studentRepo;
    private final IRepository<Teacher> teacherRepo;

    public GrammarService(IRepository<Grammar> grammarRepo, IRepository<Student> studentRepo, IRepository<Teacher> teacherRepo) {
        this.grammarRepo = grammarRepo;
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

    public Grammar getGrammarById(Integer grammarId){
        for (Grammar grammar : grammarRepo.getAll()) {
            if (grammar.getId().equals(grammarId))
                return grammar;
        }
        return null;
    }

    public static String[][] appendRow(String[][] originalMatrix, String[] newRow) {
        if (originalMatrix==null||originalMatrix.length==0)
        {
            String[][] newMatrix = new String[1][100];
            newMatrix[0]=newRow;
            return newMatrix;
        }

        int numRows = originalMatrix.length;
        int numCols = originalMatrix[0].length;

        String[][] newMatrix = new String[numRows + 1][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                newMatrix[i][j] = originalMatrix[i][j];
            }
        }

        for (int j = 0; j < numCols; j++) {
            newMatrix[numRows][j] = newRow[j];
        }

        return newMatrix;
    }

    public void enroll(Integer studentId, Integer grammarCourseId) {
        int alreadyEnrolled=0;

        Student student = getStudentById(studentId);
        Grammar course = getGrammarById(grammarCourseId);

        for (Course course1:student.getCourses()){
            if (course1.getId().equals(grammarCourseId))
                alreadyEnrolled=1;
        }

        if (alreadyEnrolled==0){
            studentRepo.delete(studentId);
            grammarRepo.delete(grammarCourseId);
            if (course.getAvailableSlots() > course.getEnrolledStudents().size()) {
                course.getEnrolledStudents().add(student);
                student.getCourses().add(course);
                grammarRepo.create(course);
                studentRepo.create(student);
            }
        }

    }


    public void practiceGrammar(Integer studentId, Integer courseId) {

        Student student = getStudentById(studentId);
        Grammar course = getGrammarById(courseId);
        String []exercise;
        String[][] exercises=course.getExercises();
        Scanner scanner = new Scanner(System.in);
        int correctAnswers=0;


        int foundCourse=0;
        for (Course findCourse : student.getCourses()){
            if (findCourse.getId().equals(courseId))
            {
                foundCourse=1;
                break;}
        }
        if (foundCourse==0){
            System.out.println("\n\n\nYou are not enrolled in this course!");}
        if(foundCourse==1){
            System.out.println("PLease fill in the gaps with the correct word:");
            for(int i=0; i<10; i++){
                exercise=exercises[i];
                System.out.println("Question "+i+": "+exercise[0]);
                System.out.print("Answer: ");
                String answer=scanner.nextLine();
                if(answer.equals(exercise[1])) {
                    correctAnswers++;
                    System.out.println("Corect!");
                }
                else{
                    System.out.println(answer+" "+exercise[1]+" is wrong");
                    student.setPastGrammarMistakes(appendRow(student.getPastGrammarMistakes(), exercise ));
                    studentRepo.update(student);
                }
            }
            if(correctAnswers>5) System.out.println("You have passed this practice test with the grade "+correctAnswers+"!");
            else System.out.println("You have failed this practice test with the grade "+correctAnswers+". Do better, loser");
        }

    }

    public void reviewPastMistakes(Integer studentId, Integer courseId) {

        Student student = getStudentById(studentId);
        Grammar course = getGrammarById(courseId);
        String[][] newMistakes=new String[1][2];
        String[][] pastGrammarMistakes = student.getPastGrammarMistakes();
        String[] exercise;
        Scanner scanner = new Scanner(System.in);
        int foundCourse = 0;
        for (Course findCourse : student.getCourses()) {
            if (findCourse.getId().equals(courseId)) {
                foundCourse = 1;
                break;
            }
        }
        if (foundCourse == 0)
            System.out.println("\n\n\nYou are not enrolled in this course!");
        if (foundCourse == 1) {
            System.out.println("PLease fill in the gaps with the correct word:");
            for (int i = 1; i <student.getPastGrammarMistakes().length; i++) {
                exercise = pastGrammarMistakes[i];
                System.out.println("Question " + i + ": " + exercise[0]);
                System.out.print("Answer: ");
                String answer = scanner.nextLine();
                if (!answer.equals(exercise[1]))
                    newMistakes=appendRow(newMistakes, exercise);
            }
            student.setPastGrammarMistakes(newMistakes);
            studentRepo.update(student);
        }
    }

    public List<Student> getAllStudents() {
        return studentRepo.getAll();
    }

    /**
     *
     * @return all grammar courses
     */
    public void getAvailableCourses() {
        for (Grammar grammar:grammarRepo.getAll())
            System.out.println(grammar);
    }

    public List<Student> getEnrolledStudents(Integer courseId) {

        Grammar course = getGrammarById(courseId);
        return course.getEnrolledStudents();
    }

    public void removeCourse(Integer courseId, Integer teacherId) {

        Grammar course = getGrammarById(courseId);
        if (course.getTeacher().getId().equals(teacherId)) {
            grammarRepo.delete(courseId);
        } else {
            System.out.println("You don't have access to this course!");
        }
    }

    public void viewCourseTaughtByTeacher(Integer teacherId) {

        for (Grammar course : grammarRepo.getAll()) {
            if (course.getTeacher().getId().equals(teacherId)) {
                System.out.println(course.getCourseName());
            }
        }
    }

    public void createOrUpdateGrammarCourse(Integer courseId, Integer teacherId, String courseName, Integer maxStudents) {
        int found = 0;
        for (Grammar course : grammarRepo.getAll()) {
            if (course.getId().equals(courseId)) {
                found = 1;
                updateGrammarCourse(courseId, teacherId, courseName, maxStudents);
                return;
            }
        }
        if (found == 0) {
            createGrammarCourse(courseId, teacherId, courseName, maxStudents);
        }
    }

    public void createGrammarCourse(Integer courseId, Integer teacherId, String courseName, Integer maxStudents) {

        Teacher teacher = getTeacherById(teacherId);
        Grammar g1 = new Grammar(courseId, courseName, teacher, maxStudents);
        grammarRepo.create(g1);
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
        g1.setExercises(grammarExercises);
        grammarRepo.update(g1);
    }

    public void updateGrammarCourse(Integer courseId, Integer teacherId, String courseName, Integer maxStudents) {

        Grammar course = getGrammarById(courseId);
        Teacher teacher = getTeacherById(teacherId);
        grammarRepo.delete(courseId);
        Grammar g1 = new Grammar(courseId, courseName, teacher, maxStudents);
        grammarRepo.create(g1);
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
        g1.setExercises(grammarExercises);
        grammarRepo.update(g1);
    }

    public void showStudentsEnrolledInGrammarCourses(){
        for(Student student:studentRepo.getAll())
            for(Course course:student.getCourses())
                if(course.getCourseName().contains("Grammar"))
                {
                    System.out.println(student);
                    break;
                }
    }

    /**
     * Displays all grammar courses a student is enrolled in
     * @param studentId identifies a student
     */
    public void showEnrolledGrammarCourses(Integer studentId){
        Student student = getStudentById(studentId);
        for (Course course:student.getCourses())
            if (course.getCourseName().contains("Grammar"))
                System.out.println(course);
    }
}
