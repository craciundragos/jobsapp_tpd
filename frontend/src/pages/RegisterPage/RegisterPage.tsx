import styles from "./RegisterPage.module.css";
import {useEffect, useState} from "react";
import {registerUser} from "../../services/AuthService";
import {Link} from "react-router-dom";
import {loginUser} from "../../services/AuthService";


function RegisterPage() {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [role, setRole] = useState(0);
    const [error, setError] = useState("");

    useEffect(() => {
        document.title = "Register Page";
    }, []);

    const handleRegister= async (e: React.FormEvent) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            setError("Parolele nu coincid");
            return;
        }
        try {
            const userData = await registerUser(username, email, password, role);
            console.log("User data:", userData);
            console.log("inregistrare reusita")
            const loginData = await loginUser(email, password);
            console.log("Login data:", loginData);
            window.location.href = "/home";
        }catch (error: any){
            setError(error.message)
        }
    }

    console.log("Username:", email);
    console.log("Password:", password);
    return (
        <div className={styles.container}>
            <h1 className={styles.title}>AiJobs App</h1>
            <h2 className={styles.title}>Register</h2>

            <form onSubmit={handleRegister} className={styles.form}>
                <input
                    className={styles.input}
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => {
                        setUsername(e.target.value);
                        if (error) setError("");
                    }}
                />
                <input
                    className={styles.input}
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => {
                        setEmail(e.target.value);
                        if (error) setError("");
                    }}
                />
                <input
                    className={styles.input}
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => {
                        setPassword(e.target.value);
                        if (error) setError("");
                    }}
                />
                <input
                    className={styles.input}
                    type="password"
                    placeholder="Confirm password"
                    value={confirmPassword}
                    onChange={(e) => {
                        setConfirmPassword(e.target.value);
                        if (error) setError("");
                    }}
                />
                <label>
                    Create recruiter account?
                    <input
                        type="checkbox"
                        checked={role === 1}
                        onChange={(e) => setRole(e.target.checked ? 1 : 0)}
                    />
                </label>
                <br/>
                {error && <p className={styles.error}>{error}</p>}

                <button className={styles.button} type="submit">
                    Register
                </button>
            </form>

            <p>
                Already have an account?{" "}
                <Link to="/login" className={styles.registerLink}>
                    Login
                </Link>
            </p>
        </div>
    );
}

export default RegisterPage;
