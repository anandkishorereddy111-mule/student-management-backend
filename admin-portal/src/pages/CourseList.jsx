import { useEffect, useState } from "react";
import { getCourses, deleteCourse } from "../api/courseApi";

export default function CourseList({onEdit}) {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchCourses = async () => {
    setLoading(true);
    try {
      const res = await getCourses();
      setCourses(res.data);
    } catch (err) {
      console.error("Failed to fetch courses", err);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchCourses();
  }, []);

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this course?")) return;
    await deleteCourse(id);
    fetchCourses();
  };

  if (loading) return <p>Loading courses...</p>;

  return (
    <div style={{ padding: "20px" }}>
      <h2>Courses</h2>
      <table border="1" cellPadding="8" style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th>ID</th><th>Name</th><th>Price</th><th>Active</th><th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {courses.map((c) => (
            <tr key={c.courseId}>
              <td>{c.courseId}</td>
              <td>{c.courseName}</td>
              <td>{c.price}</td>
              <td>{c.active ? "Yes" : "No"}</td>
              <td>
  <button onClick={() => onEdit(c)}>Edit</button>
  <button onClick={() => handleDelete(c.courseId)} style={{ marginLeft: "8px" }}>Delete</button>
</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}