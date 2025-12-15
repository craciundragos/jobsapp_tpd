import styles from "./LoginPage.module.css";
import {useEffect, useState} from "react";
import {loginUser} from "../../services/AuthService";
import {Link} from "react-router-dom";



function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errors, setErrors] = useState<{ [key: string]: string }>({});


    useEffect(() => {
        document.title = "Login Page";
    }, []);

    const handleLogin= async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const userData = await loginUser(email, password);
            console.log("User data:", userData);
            console.log("Login reusit")
            window.location.href = "/home";
        }catch (error: any){
            if (typeof error === "object") {
                setErrors(error);
            }
            else {
                setErrors({message: error.message || "Login failed"} );
            }
        }
    }

    console.log("Username:", email);
    console.log("Password:", password);
    return (
            <div className={styles.container}>
                <h1 className={styles.title}>AiJobs App</h1>
                <h2 className={styles.title}>Login</h2>

                <form onSubmit={handleLogin} className={styles.form}>
                    <input
                        className={styles.input}
                        type="text"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => {
                            setEmail(e.target.value);
                            if (errors) setErrors({...errors, email:""});

                        }}
                    />
                    {errors.email && <p className={styles.error}>{errors.email}</p>}
                    <input
                        className={styles.input}
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => {
                            setPassword(e.target.value);
                            if (errors) setErrors({...errors, password:""});
                        }}
                    />
                    {errors.password && <p className={styles.error}>{errors.password}</p>}
                    {errors.message && <p className={styles.error}>{errors.message}</p>}

                    <button className={styles.button} type="submit">
                        Login
                    </button>

                </form>

                <p>
                    Not registered?{" "}
                    <Link to="/register" className={styles.registerLink}>
                        Register
                    </Link>
                </p>
            </div>
    );
}

export default LoginPage;
