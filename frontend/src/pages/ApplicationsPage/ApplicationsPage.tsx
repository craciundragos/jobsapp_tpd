import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getApplications } from "../../services/ApplicationService";
import styles from "./ApplicationsPage.module.css";
import {openResume} from "../../services/ResumeService.ts";

interface ApplicationDTO {
    id: number;
    userId: number;
    jobId: number;
    resumeUrl: string;
    status: string;
    rankAi: number;
    explanation: string;
}

export default function ApplicationsPage() {
    const { jobId } = useParams();
    const [applications, setApplications] = useState<ApplicationDTO[]>([]);
    const [error, setError] = useState<string>("");

    useEffect(() => {
        if (!jobId) return;

        getApplications(Number(jobId))
            .then(setApplications)
            .catch((err) => setError(err.message));
    }, [jobId]);
    console.log("Applications:", applications);

    return (
        <div className={styles.page}>
            <h1 className={styles.title}>Applications for Job #{jobId}</h1>

            <Link to="/jobs" className={styles.backBtn}>← Back to Jobs</Link>

            {error && <p className={styles.error}>{error}</p>}

            <div className={styles.list}>
                {applications.length === 0 && !error && (
                    <p className={styles.noApps}>No applications found.</p>
                )}

                {applications.map((app) => (
                    <div key={app.id} className={styles.card}>
                        <p><strong>Candidate ID:</strong> {app.userId}</p>
                        <p><strong>Status:</strong> {app.status}</p>
                        <p><strong>AI Score:</strong> {app.rankAi ?? "not scored"}</p>
                        <p><strong>AI Review:</strong> {app.explanation ?? "not scored"} </p>
                        <button onClick={() => openResume(app.resumeUrl)}>
                            View Resume
                        </button>

                    </div>
                ))}
            </div>
        </div>
    );
}
