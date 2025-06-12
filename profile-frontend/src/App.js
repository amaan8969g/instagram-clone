import React, { useState } from "react";
import Login from "./components/Login";
import Signup from "./components/Signup";
import Profile from "./components/Profile";
import "./components/Auth.css";

function App() {
  const [page, setPage] = useState("login");
  const [user, setUser] = useState(null);

  if (user) return <Profile user={user} />;

  return (
    <div>
      {page === "login" && <Login onLogin={setUser} onSwitch={() => setPage("signup")} />}
      {page === "signup" && <Signup onSwitch={() => setPage("login")} />}
    </div>
  );
}

export default App;
