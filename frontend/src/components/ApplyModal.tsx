import { useState } from "react";
import styles from "./ApplyModal.module.css";

interface ApplyModalProps {
    jobTitle: string;
    onClose: () => void;
    onSubmit: (resume: File) => void;
    loading?: boolean;
}

export default function ApplyModal({ jobTitle, onClose, onSubmit, loading = false }: ApplyModalProps) {
    const [resumeFile, setResumeFile] = useState<File | null>(null);
    const [error, setError] = useState("");

    const handleSubmit = () => {
        if (!resumeFile) {
            setError("Selectează un fișier CV înainte de a aplica.");
            return;
        }
        setError("");
        onSubmit(resumeFile);
    };

    return (
        <div className={styles.modalOverlay} onClick={onClose}>
            <div
                className={styles.modal}
                onClick={(e) => e.stopPropagation()}
            >
                <h2 className={styles.modalTitle}>
                    Aplică la: <span className={styles.modalJob}>{jobTitle}</span>
                </h2>

                <label className={styles.fileLabel}>
                    Add your Resume (as PDF)
                    <input
                        type="file"
                        accept=".pdf"
                        onChange={(e) => setResumeFile(e.target.files?.[0] || null)}
                    />
                </label>

                {resumeFile && (
                    <p className={styles.fileInfo}>
                        Fișier selectat: <strong>{resumeFile.name}</strong>
                    </p>
                )}

                {error && <p className={styles.error}>{error}</p>}

                <div className={styles.modalActions}>
                    <button
                        className={styles.secondaryBtn}
                        onClick={onClose}
                        disabled={loading}
                    >
                        Anulează
                    </button>

                    <button
                        className={styles.primaryBtn}
                        onClick={handleSubmit}
                        disabled={loading}
                    >
                        {loading ? "Se trimite..." : "Trimite aplicația"}
                    </button>
                </div>
            </div>
        </div>
    );
}