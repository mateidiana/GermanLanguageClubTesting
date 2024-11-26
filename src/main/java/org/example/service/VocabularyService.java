package org.example.service;
import java.util.Scanner;
import java.util.List;
import java.util.*;

import org.example.model.*;
import org.example.model.Vocabulary;
import org.example.repo.*;
public class VocabularyService {
    private final IRepository<Vocabulary> vocabRepo;
    private final IRepository<Student> studentRepo;
    private final IRepository<Teacher> teacherRepo;

    public VocabularyService(IRepository<Vocabulary> vocabRepo, IRepository<Student> studentRepo, IRepository<Teacher> teacherRepo ) {
        this.vocabRepo = vocabRepo;
        this.studentRepo = studentRepo;
        this.teacherRepo= teacherRepo;
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

    public Vocabulary getVocabById(Integer vocabId){
        for (Vocabulary vocab : vocabRepo.getAll()) {
            if (vocab.getId().equals(vocabId))
                return vocab;
        }
        return null;
    }

    public void enroll(Integer studentId, Integer vocabCourseId) {
        int alreadyEnrolled=0;

        Student student = getStudentById(studentId);
        Vocabulary course = getVocabById(vocabCourseId);

        for (Course course1:student.getCourses()){
            if (course1.getId().equals(vocabCourseId))
                alreadyEnrolled=1;
        }

        if (alreadyEnrolled==0){
            studentRepo.delete(studentId);
            vocabRepo.delete(vocabCourseId);
            if (course.getAvailableSlots() > course.getEnrolledStudents().size()) {
                course.getEnrolledStudents().add(student);
                student.getCourses().add(course);
                vocabRepo.create(course);
                studentRepo.create(student);
            }
        }

    }

    public void practiceVocabulary(Integer studentId, Integer courseId) {

        Student student = getStudentById(studentId);
        Vocabulary course = getVocabById(courseId);
        Scanner scanner = new Scanner(System.in);
        int correctAnswers=0;
        int foundCourse=0;
        Map <String, String> tempother=new HashMap<>();
        for (Course findCourse : student.getCourses()){
            if (findCourse.getId().equals(courseId))
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
            for(int i=0; i<10; i++){
                List<String> values = new ArrayList<>(course.getWorter().values());
                Random random = new Random();
                int randomIndex = random.nextInt(values.size());
                String ubung = values.get(randomIndex);
                System.out.println(ubung+": ");
                String answer=scanner.nextLine();
                boolean found=false;
                for (Map.Entry<String, String> entry : course.getWorter().entrySet()) {
                    String key=entry.getKey();
                    String value=entry.getValue();
                    if(value.equals(ubung) && key.equals(answer)){
                        System.out.println("Correct!");
                        found=true;
                    }
                    else{
                        placeholderKey=key;
                        placeholderKey=value;
                    }
                }
                if(found==true)
                    correctAnswers++;
                else{
                    tempother=student.getPastVocabMistakes();
                    tempother.put(placeholderKey, placeholderValue);
                    student.setPastVocabMistakes(tempother);
                    studentRepo.update(student);
                }
            }
            if(correctAnswers>5) System.out.println("You have passed this practice test with the grade "+correctAnswers+"!");
            else System.out.println("You have failed this practice test with the grade "+correctAnswers+". Do better, loser");
        }


    }

    public void reviewPastMistakes(Integer studentId, Integer courseId) {

        Student student = getStudentById(studentId);
        Vocabulary course = getVocabById(courseId);
        Map <String, String> tempother=new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        int foundCourse = 0;
        for (Course findCourse : student.getCourses()) {
            if (findCourse.getId().equals(courseId)) {
                foundCourse = 1;
                break;
            }
        }
        if (foundCourse == 0) {
            System.out.println("\n\n\nYou are not enrolled in this course!");
        }
        if (foundCourse == 1) {
            System.out.println("PLease fill in the gaps with the correct word(use capital letter when noun):");
            String placeholderKey = new String();
            String placeholderValue = new String();
            for (Map.Entry<String, String> entry : course.getWorter().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println(value+": ");
                String answer = scanner.nextLine();
                if (answer.equals(key)) {
                    System.out.println("Correct!");
                } else {
                    tempother.put(key, value);
                }
            }
            student.setPastVocabMistakes(tempother);
        }
    }

    public void getAvailableCourses() {
        for (Vocabulary vocab:vocabRepo.getAll())
            System.out.println(vocab);
    }

    public List<Student> getEnrolledStudents(Integer courseId) {

        Vocabulary course = getVocabById(courseId);
        return course.getEnrolledStudents();
    }

    public List<Student> getAllStudents() {
        return studentRepo.getAll();
    }

    public void viewCourseTaughtByTeacher(Integer teacherId) {
        for (Vocabulary course : vocabRepo.getAll()) {
            if (course.getTeacher().getId().equals(teacherId)) {
                System.out.println(course.getCourseName());
            }
        }
    }

    public void createOrUpdateVocabularyCourse(Integer courseId, Integer teacherId, String courseName, Integer maxStudents) {
        int found = 0;
        for (Vocabulary course : vocabRepo.getAll()) {
            if (course.getId().equals(courseId)) {
                found = 1;
                updateVocabularyCourse(courseId, teacherId, courseName, maxStudents);
                return;
            }
        }
        if (found == 0) {
            createVocabularyCourse(courseId, teacherId, courseName, maxStudents);
        }
    }

    public void createVocabularyCourse(Integer courseId, Integer teacherId, String courseName, Integer maxStudents) {

        Teacher teacher=getTeacherById(teacherId);
        Vocabulary v1 = new Vocabulary(courseId, courseName, teacher, maxStudents);
        vocabRepo.create(v1);
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
        v1.setWorter(vocabularyExercises);
        vocabRepo.update(v1);
    }

    public void updateVocabularyCourse(Integer courseId, Integer teacherId, String courseName, Integer maxStudents) {

        Teacher teacher=getTeacherById(teacherId);
        Vocabulary course = getVocabById(courseId);
        vocabRepo.delete(courseId);
        Vocabulary v1 = new Vocabulary(courseId, courseName, teacher, maxStudents);
        vocabRepo.create(v1);
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
        v1.setWorter(vocabularyExercises);
        vocabRepo.update(v1);
    }


    public void removeVocabularyCourse(Integer courseId, Integer teacherId) {
        Vocabulary course = getVocabById(courseId);
        if (course.getTeacher().getId().equals(teacherId)) {
            vocabRepo.delete(courseId);
        } else {
            System.out.println("You don't have access to this course!");
        }
    }

    public void showStudentsEnrolledInVocabCourses(){
        for(Student student:studentRepo.getAll())
            for(Course course:student.getCourses())
                if(course.getCourseName().contains("Vocab"))
                {
                    System.out.println(student);
                    break;
                }
    }

    /**
     * Displays all vocabulary courses a student is enrolled in
     * @param studentId identifies a student
     */
    public void showEnrolledVocabCourses(Integer studentId){
        Student student = getStudentById(studentId);
        for (Course course:student.getCourses())
            if (course.getCourseName().contains("Vocab"))
                System.out.println(course);
    }

}
