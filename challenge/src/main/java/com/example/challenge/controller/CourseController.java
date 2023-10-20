package com.example.challenge.controller;

import com.example.challenge.model.Course;
import com.example.challenge.model.LowCourseGpa;
import com.example.challenge.model.MediumCourseGpa;
import com.example.challenge.model.HighCourseGpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@ComponentScan(basePackages = "com.example.challenge.model")

public class CourseController {

    private List<Course> courses = new ArrayList<>();
    private LowCourseGpa lowCourseGpa;
    private MediumCourseGpa mediumCourseGpa;
    private HighCourseGpa highCourseGpa;

    @Autowired
    public CourseController(LowCourseGpa lowCourseGpa, MediumCourseGpa mediumCourseGpa, HighCourseGpa highCourseGpa) {
        this.lowCourseGpa = lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }

    @RequestMapping(value = "/courses",method = RequestMethod.GET)
    public List<Course> getAll(){
        return courses;
    }

    @RequestMapping(value = "/courses/{name}",method = RequestMethod.GET)
    public Course getCourseByName(@PathVariable String name) {
        return courses.stream()
                .filter(course -> course.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @RequestMapping(value = "/courses",method = RequestMethod.POST)
    public Map<String, Object> addCourse(@RequestBody Course course) {
        boolean isExist=courses.stream().anyMatch(c -> c.getName().equals(course.getName()));

        if(isExist){
            throw new RuntimeException("Already exist");
        }

        int credit = course.getCredit();
        if (credit < 0 || credit > 4) {
            throw new RuntimeException ("Credit value must be between 0 and 4");
        }

        int totalGpa = calculateTotalGpa(course);
        courses.add(course);

        Map<String, Object> response = Map.of(
                "course", course,
                "totalGpa", totalGpa
        );

        return response;
    }


    @RequestMapping(value = "/courses/{name}", method = RequestMethod.PUT)
    public Map<String, Object> updateCourseByName(@PathVariable String name, @RequestBody Course newCourse) {
        // İlgili ada sahip kursu bul
        Course existingCourse = null;
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                existingCourse = course;
                break;
            }
        }

        if (existingCourse == null) {
            throw new RuntimeException("Course with name " + name + " not found");
        }

        // Yeni kurs bilgileri ile güncelle
        existingCourse.setName(newCourse.getName());
        existingCourse.setCredit(newCourse.getCredit());
        existingCourse.setGrade(newCourse.getGrade());

        int totalGpa = calculateTotalGpa(existingCourse);

        Map<String, Object> response = Map.of(
                "course", existingCourse,
                "totalGpa", totalGpa
        );

        return response;
    }


    @RequestMapping(value = "/courses/{name}", method = RequestMethod.DELETE)
    public void deleteCourseByName(@PathVariable String name) {
        Course courseToDelete = null;
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                courseToDelete = course;
                break;
            }
        }

        if (courseToDelete == null) {
            throw new RuntimeException("Course with name " + name + " not found.");
        }

        courses.remove(courseToDelete);
    }


    private int calculateTotalGpa(Course course) {
        int credit = course.getCredit();
        int coefficient = course.getGrade().getCoefficient();

        int gpa;
        if (credit <= 2) {
            gpa = lowCourseGpa.getGpa();
        } else if (credit == 3) {
            gpa = mediumCourseGpa.getGpa();
        } else {
            gpa = highCourseGpa.getGpa();
        }

        return coefficient * credit * gpa;
    }



}
