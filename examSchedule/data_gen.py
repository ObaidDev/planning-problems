from datetime import datetime, timedelta
import uuid
import random
import json

# Constants
COURSE_TYPES = ["MATHEMATICS", "PHYSICS", "CHEMISTRY", "BIOLOGY", "COMPUTER_SCIENCE"]

def generate_exam_schedule_data_with_students(min_students_per_course, max_students_per_course):
    # Parameters
    num_rooms = 8
    num_time_slots = 2
    num_courses = 5
    num_students = 200
    min_room_capacity = 40

    # Helper Functions
    def random_time_slots():
        start_date = datetime(2024, 12, 31, 8, 0)  # Starting date and time
        slots = []
        days = 3  # Number of different days

        for day in range(days):
            start = start_date + timedelta(days=day)  # Increment day
            for _ in range(num_time_slots):  # Generate time slots for the day
                end = start + timedelta(hours=2)
                slots.append({
                    "id": str(uuid.uuid4()),
                    "startTime": start.isoformat(),
                    "endTime": end.isoformat()
                })
                start = end + timedelta(minutes=30)  # 30-minute break between slots

        return slots

    def random_courses():
        return [
            {
                "id": str(uuid.uuid4()),
                "coursType": random.choice(COURSE_TYPES),
                "numberOfStudents": 0  # Will be updated later
            }
            for _ in range(num_courses)
        ]

    def random_students(courses):
        students = []
        course_enrollment = {course["id"]: [] for course in courses}

        for _ in range(num_students):
            student_id = str(uuid.uuid4())
            enrolled_courses = set()

            while len(enrolled_courses) < random.randint(1, 3):
                course = random.choice(courses)
                if course["id"] not in enrolled_courses:
                    enrolled_courses.add(course["id"])
                    course["numberOfStudents"] += 1
                    course_enrollment[course["id"]].append(student_id)

            student = {
                "id": student_id,
                "name": f"Student_{student_id[:8]}",
                "coursesEnrolled": [
                    {
                        "id": course_id,
                        "coursType": next(c["coursType"] for c in courses if c["id"] == course_id),
                        "numberOfStudents": next(c["numberOfStudents"] for c in courses if c["id"] == course_id)
                    }
                    for course_id in enrolled_courses
                ]
            }
            students.append(student)

        # Ensure minimum students per course
        for course in courses:
            while course["numberOfStudents"] < min_students_per_course:
                extra_student = random.choice(students)
                if not any(c["id"] == course["id"] for c in extra_student["coursesEnrolled"]):
                    extra_student["coursesEnrolled"].append({
                        "id": course["id"],
                        "coursType": course["coursType"],
                        "numberOfStudents": course["numberOfStudents"] + 1
                    })
                    course["numberOfStudents"] += 1
                    course_enrollment[course["id"]].append(extra_student["id"])

            # Ensure maximum students per course
            while course["numberOfStudents"] > max_students_per_course:
                # Remove students if the course exceeds max students
                student_to_remove = random.choice(course_enrollment[course["id"]])
                course_enrollment[course["id"]].remove(student_to_remove)
                course["numberOfStudents"] -= 1
                # Remove this course from the student's list
                student = next(student for student in students if student["id"] == student_to_remove)
                student["coursesEnrolled"] = [c for c in student["coursesEnrolled"] if c["id"] != course["id"]]

        return students

    def random_exams(courses, rooms, time_slots, students):
        exams = []
        for course in courses:
            room = random.choice(rooms)
            time_slot = random.choice(time_slots)

            eligible_students = [
                student for student in students
                if any(c["id"] == course["id"] for c in student["coursesEnrolled"])
            ]

            if not eligible_students:
                continue

            exam = {
                "id": str(uuid.uuid4()),
                "course": {
                    "id": course["id"],
                    "coursType": course["coursType"],
                    "numberOfStudents": len(eligible_students),
                    "studentsEnrolled": [
                        {
                            "id": student["id"],
                            "name": student["name"],
                            "coursesEnrolled": [
                                c for c in student["coursesEnrolled"] if c["id"] == course["id"]
                            ]
                        }
                        for student in eligible_students
                    ]
                },
                "room": room,
                "timeSlot": time_slot
            }
            exams.append(exam)
        return exams

    # Generate data
    rooms = [{"id": str(uuid.uuid4()), "name": f"Room_{i+1}", "capacity": random.randint(min_room_capacity, 40)} for i in range(num_rooms)]
    time_slots = random_time_slots()
    courses = random_courses()
    students = random_students(courses)
    exams = random_exams(courses, rooms, time_slots, students)

    return {
        "rooms": rooms,
        "timeSlots": time_slots,
        "courses": courses,
        "exams": exams
    }

# Set your desired min and max number of students per course here
min_students_per_course = 28
max_students_per_course = 40

# Generate and save dataset
dataset = generate_exam_schedule_data_with_students(min_students_per_course, max_students_per_course)
output_path = "./data/EXAM_SCHEDULE_DUMMY_DATA.json"
with open(output_path, "w") as f:
    json.dump(dataset, f, indent=4)
print(f"Dataset saved to {output_path}")
