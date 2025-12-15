import styles from "./CompanyPage.module.css";
import {Link} from "react-router-dom";
import {useState, useEffect} from "react";
import {createJob, getAllRecruiterJobs} from "../../services/JobService.ts";
import AddJobModal from "../../components/AddJobModal.tsx";
import AddCompanyModal from "../../components/AddCompanyModal.tsx";
import {createCompany, getRecruiterCompanies} from "../../services/CompanyService.ts";
import { useNavigate } from "react-router-dom";
import AddRequirementsModal from "../../components/AddRequirementsModal.tsx";
import {saveJobRequirements} from "../../services/JobRequirementsService.ts";
import { logout } from "../../services/AuthService";

export default function CompanyPage() {
    const [jobs, setJobs] = useState<JobDTO[]>([]);
    interface JobDTO {
        id: number;
        title: string;
        description: string;
        status: string;
        companyId: number;
        companyName: string;
    }
    interface CompanyDTO {
        id: number;
        name: string;
        recruiterId: number;
    }

    const [showAddJob, setShowAddJob] = useState(false);
    const [showAddCompany, setShowAddCompany] = useState(false);
    const [companies, setCompanies] = useState<CompanyDTO[]>([]);
    const navigate = useNavigate();
    const [showRequirementsModal, setShowRequirementsModal] = useState(false);
    const [currentJobId, setCurrentJobId] = useState<number | null>(null);
    const handleLogout = () => {
        logout();
        navigate("/login", { replace: true });
    };

    useEffect(() => {
        Promise.all([getRecruiterCompanies(), getAllRecruiterJobs()])
            .then(([companiesData, jobsData]) => {
                setCompanies(companiesData);
                setJobs(jobsData);
            })
            .catch(console.error);
    }, []);
    console.log("Jobs:", jobs);

    return (
        <div className={styles.page}>
            <nav className={styles.navbar}>
                <div className={styles.leftSection}>
                    <div className={styles.logo}>AiJobs App</div>
                    {/*<input*/}
                    {/*    type="text"*/}
                    {/*    className={styles.searchInput}*/}
                    {/*    placeholder="Caută joburi..."*/}
                    {/*    // value={searchTerm}*/}
                    {/*    // onChange={(e) => setSearchTerm(e.target.value)}*/}
                    {/*/>*/}
                </div>
                <div className={styles.links}>
                    <Link to="/home" className={styles.link}>
                        Joburi
                    </Link>
                    <button onClick={handleLogout} className={styles.logout}>
                        Logout
                    </button>
                </div>
            </nav>
            <div className={styles.content}>
                <div className={styles.headerRow}>
                    <h1 className={styles.title}>Jobs you recruit</h1>

                    <div className={styles.actions}>
                        <button className={styles.addButton} onClick={() => setShowAddJob(true)}>
                            Add Job
                        </button>
                        <button className={styles.addButton} onClick={() => setShowAddCompany(true)}>
                            Add Company
                        </button>
                    </div>
                </div>

                <div className={styles.jobsGrid}>
                    {jobs.map((job) => (
                        <div key={job.id} className={styles.card}>
                            <h2 className={styles.jobTitle}>{job.title}</h2>
                            <p className={styles.company}>Company: {job.companyName}</p>
                            <p className={styles.description}>{job.description}</p>

                            <p className={styles.status}>
                                Status:{" "}
                                <span className={job.status === "open" ? styles.open : styles.closed}>
                            {job.status}
        </span>
                            </p>

                            <div className={styles.cardActions}>
                                <button
                                    className={styles.applyButton}
                                    disabled={job.status !== "open"}
                                    onClick={() => navigate(`/applications/${job.id}`)}
                                >
                                    See applications
                                </button>

                                <button
                                    className={styles.addButton}
                                    onClick={() => {
                                        setCurrentJobId(job.id);
                                        setShowRequirementsModal(true);
                                    }}
                                >
                                    Add Requirements
                                </button>
                            </div>
                        </div>
                    ))}

                </div>
            </div>
            <AddJobModal
                visible={showAddJob}
                companies={companies}
                onSubmit={async (job) => {
                    try {
                        const created = await createJob(job);
                        console.log("CREATED JOB:", created);
                        const jobsData = await getAllRecruiterJobs();
                        setJobs(jobsData);
                        setShowAddJob(false);
                    } catch (error) {
                        console.error(error);
                        alert("Failed to create job.");
                    }
                }}
                onClose={() => setShowAddJob(false)}
            />

            <AddCompanyModal
                visible={showAddCompany}
                onClose={() => setShowAddCompany(false)}
                onSubmit={async (company) => {
                    try {
                        const createdCompany = await createCompany(company);
                        console.log("Created company:", createdCompany);
                        setShowAddCompany(false);
                    }catch (error) {
                        console.error(error)
                        alert("Failed to create company.");
                    }
                }}
            />
            <AddRequirementsModal
                visible={showRequirementsModal}
                onClose={() => setShowRequirementsModal(false)}
                onSubmit={async (form) => {
                    try {
                        await saveJobRequirements(currentJobId!, form);
                        alert("Requirements saved!");
                        setShowRequirementsModal(false);
                    } catch (e) {
                        alert("Failed to save requirements");
                    }
                }}
            />

        </div>
    );
}