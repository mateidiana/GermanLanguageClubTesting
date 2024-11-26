package org.example.service;
import org.example.model.Student;
import org.example.model.Teacher;
import org.example.repo.IRepository;
public class TeacherService {
    private final IRepository<Teacher> teacherRepo;
    public TeacherService(IRepository<Teacher> teacherRepo){
        this.teacherRepo=teacherRepo;
    }
    public void createTeacher(Integer teacherId, String name){
        for(Teacher teacher:teacherRepo.getAll())
            if (teacher.getId().equals(teacherId)){
                System.out.println("Id already in use!");
                return;
            }

        Teacher teacher = new Teacher(name,teacherId);
        teacherRepo.create(teacher);
        System.out.println("Registration successful!");
    }

    public Teacher getTeacherById(Integer teacherId){
        for (Teacher teacher : teacherRepo.getAll()) {
            if (teacher.getId().equals(teacherId))
                return teacher;
        }
        return null;
    }
}
