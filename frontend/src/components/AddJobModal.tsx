import { useState } from "react";
import styles from "./ApplyModal.module.css";

interface CompanyDTO {
    id: number;
    name: string;
    recruiterId: number;
}

interface AddJobModalProps {
    visible: boolean;
    onClose: () => void;
    companies: CompanyDTO[];
    onSubmit: (job: {
        title: string;
        description: string;
        companyId: number;
    }) => void;
}

export default function AddJobModal({ visible, onClose, companies, onSubmit }: AddJobModalProps) {
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [companyId, setCompanyId] = useState<number>(0);

    if (!visible) return null;

    const handleSubmit = () => {
        if (!companyId) {
            alert("Please select a company.");
            return;
        }
        onSubmit({ title, description, companyId });
    };

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modal}>
                <h2 className={styles.modalTitle}>Add Job</h2>

                <label className={styles.fileLabel}>Title</label>
                <input
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                />

                <label className={styles.fileLabel}>Description</label>
                <textarea
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    rows={4}
                />

                <label className={styles.fileLabel}>Company</label>
                <select
                    value={companyId}
                    onChange={(e) => setCompanyId(Number(e.target.value))}
                >
                    <option value={0}>Select a company</option>
                    {companies.map((company) => (
                        <option key={company.id} value={company.id}>
                            {company.name}
                        </option>
                    ))}
                </select>

                <div className={styles.modalActions}>
                    <button className={styles.secondaryBtn} onClick={onClose}>
                        Cancel
                    </button>

                    <button className={styles.primaryBtn} onClick={handleSubmit}>
                        Add Job
                    </button>
                </div>
            </div>
        </div>
    );
}
