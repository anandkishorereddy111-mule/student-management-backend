import { useState } from "react";
import axios from "axios";
import { updateCourse } from "../api/courseApi";

const UPLOAD_URL = "http://localhost:8080/api/courses/upload-image";
const STUDENT_FRONTEND_URL = "http://localhost:5173";

export default function EditCourse({ course, onDone }) {
  const [courseName, setCourseName] = useState(course.courseName);
  const [price, setPrice] = useState(course.price);
  const [imageFile, setImageFile] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setMessage("");

    try {
      let imageFileName = course.imageFileName;

      if (imageFile) {
        const formData = new FormData();
        formData.append("file", imageFile);

        const uploadRes = await axios.post(UPLOAD_URL, formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });

        imageFileName = uploadRes.data;
      }

      await updateCourse(course.courseId, {
        courseName: courseName,
        price: parseFloat(price),
        imageFileName: imageFileName,
        active: course.active,
      });

      setMessage("Course updated successfully!");
      setTimeout(() => onDone(), 800);
    } catch (err) {
      console.error("Failed to update course", err);
      setMessage("Something went wrong. Check console.");
    }

    setSubmitting(false);
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Edit Course</h2>

      <div style={{ marginBottom: "10px" }}>
        <p>Current image:</p>
        <img
          src={`${STUDENT_FRONTEND_URL}/images/${course.imageFileName}`}
          alt={course.courseName}
          style={{ width: "150px", height: "auto" }}
        />
      </div>

      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: "10px" }}>
          <label>Course Name: </label>
          <input
            type="text"
            value={courseName}
            onChange={(e) => setCourseName(e.target.value)}
            required
          />
        </div>

        <div style={{ marginBottom: "10px" }}>
          <label>Price: </label>
          <input
            type="number"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
            required
          />
        </div>

        <div style={{ marginBottom: "10px" }}>
          <label>Replace Image (optional): </label>
          <input
            type="file"
            accept="image/*"
            onChange={(e) => setImageFile(e.target.files[0])}
          />
        </div>

        <button type="submit" disabled={submitting}>
          {submitting ? "Saving..." : "Save Changes"}
        </button>
        <button type="button" onClick={onDone} style={{ marginLeft: "10px" }}>
          Cancel
        </button>
      </form>

      {message && <p>{message}</p>}
    </div>
  );
}