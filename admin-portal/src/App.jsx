import { useState } from "react";
import CourseList from "./pages/CourseList";
import AddCourse from "./pages/AddCourse";
import EditCourse from "./pages/EditCourse";

function App() {
  const [view, setView] = useState("list");
  const [editingCourse, setEditingCourse] = useState(null);

  const handleEditClick = (course) => {
    setEditingCourse(course);
    setView("edit");
  };

  const handleDone = () => {
    setEditingCourse(null);
    setView("list");
  };

  return (
    <div>
      <nav style={{ padding: "10px", borderBottom: "1px solid #ccc" }}>
        <button onClick={() => setView("list")} disabled={view === "list"}>
          View Courses
        </button>
        <button onClick={() => setView("add")} disabled={view === "add"} style={{ marginLeft: "10px" }}>
          Add Course
        </button>
      </nav>

      {view === "list" && <CourseList onEdit={handleEditClick} />}
      {view === "add" && <AddCourse />}
      {view === "edit" && <EditCourse course={editingCourse} onDone={handleDone} />}
    </div>
  );
}

export default App;