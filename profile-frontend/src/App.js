import React, { useState } from "react";
import Login from "./components/Login";
import Signup from "./components/Signup";
import Profile from "./components/Profile";

function App() {
  const [page, setPage] = useState("login");
  const [currentUser, setCurrentUser] = useState(null);

  const handleLogin = (user) => {
    setCurrentUser(user);
    setPage("profile");
  };

  const handleLogout = () => {
    setCurrentUser(null);
    setPage("login");
  };

  const handleSignup = (user) => {
    setCurrentUser(user);
    setPage("profile");
  };

  return (
    <div>
      {page === "login" && (
        <Login 
          onLogin={handleLogin} 
          onSwitch={() => setPage("signup")} 
        />
      )}
      {page === "signup" && (
        <Signup 
          onSignup={handleSignup}
          onSwitch={() => setPage("login")} 
        />
      )}
      {page === "profile" && currentUser && (
        <Profile 
          currentUser={currentUser}
          onLogout={handleLogout}
        />
      )}
    </div>
  );
}

export default App;
