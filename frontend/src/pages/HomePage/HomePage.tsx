import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import styles from "./HomePage.module.css";
import {applyToJob, getAllJobs} from "../../services/JobService.ts";
import ApplyModal from "../../components/ApplyModal.tsx";
import { useNavigate } from "react-router-dom";
import { logout } from "../../services/AuthService";
import { hasRole } from "../../services/AuthService";

interface JobDTO {
    id: number;
    title: string;
    description: string;
    status: string;
    companyId: number;
    companyName: string;
}

export default function JobsPage() {
    const [jobs, setJobs] = useState<JobDTO[]>([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [selectedJob, setSelectedJob] = useState<JobDTO | null>(null);
    const [showModal, setShowModal] = useState(false);
    const [applyLoading, setApplyLoading] = useState(false);

    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login", { replace: true });
    };
    useEffect(() => {
        getAllJobs()
            .then(setJobs)
            .catch(err => console.error(err));
    }, []);
    console.log("Jobs:", jobs);

    const filteredJobs = jobs.filter((job) =>
        job.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        job.description.toLowerCase().includes(searchTerm.toLowerCase())
    );
    const handleSubmitApplication = async (resumeFile: File) => {
        if (!selectedJob) return;
        setApplyLoading(true);
        try {
            await applyToJob(selectedJob.id, resumeFile);
            alert("Aplicația a fost trimisă cu succes!");
            setShowModal(false);
        } catch (err: unknown) {
            if (err instanceof Error) {
                alert(err.message);
            } else {
                alert("Eroare la aplicare");
            }
        } finally {
            setApplyLoading(false);
        }
    };
    const handleApplyClick = (job: JobDTO) => {
        setSelectedJob(job);
        setShowModal(true);
    };

    return (
        <div className={styles.page}>
            <nav className={styles.navbar}>
                <div className={styles.leftSection}>
                <div className={styles.logo}>AiJobs App</div>
                <input
                    type="text"
                    className={styles.searchInput}
                    placeholder="Find jobs..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
                </div>
                <div className={styles.links}>
                    <Link to="/home">Joburi</Link>

                    {hasRole("RECRUITER") && (
                        <Link to="/jobs">Recruit</Link>
                    )}

                    <button onClick={handleLogout}>Logout</button>
                </div>
            </nav>

            <div className={styles.content}>
                <h1 className={styles.title}>Available jobs</h1>

                <div className={styles.jobsGrid}>
                    {filteredJobs.map((job) => (
                        <div key={job.id} className={styles.card}>
                            <h2 className={styles.jobTitle}>{job.title}</h2>
                            <p className={styles.company}>Company: {job.companyName}</p>
                            <p className={styles.description}>{job.description}</p>
                            <p className={styles.status}>
                                Status:{" "}
                                <span
                                    className={
                                        job.status === "OPEN" ? styles.open : styles.closed
                                    }
                                >
                  {job.status}
                </span>

                            </p>
                            <button
                                className={styles.applyButton}
                                disabled={job.status !== "OPEN"}
                                onClick={() => handleApplyClick(job)}
                            >
                                Apply
                            </button>
                        </div>
                    ))}
                    {showModal && selectedJob && (
                        <ApplyModal
                            jobTitle={selectedJob.title}
                            onClose={() => setShowModal(false)}
                            onSubmit={handleSubmitApplication}
                            loading={applyLoading}
                        />
                    )}
                </div>
            </div>
        </div>
    );
}
