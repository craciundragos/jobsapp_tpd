import { useState } from "react";
import styles from "./ApplyModal.module.css";

interface AddCompanyModalProps {
    visible: boolean;
    onClose: () => void;
    onSubmit: (companyName: string) => void;
}

export default function AddCompanyModal({ visible, onClose, onSubmit }: AddCompanyModalProps) {
    const [name, setName] = useState("");

    if (!visible) return null;

    const handleSubmit = () => {
        if (!name.trim()) {
            alert("Company name cannot be empty");
            return;
        }
        onSubmit(name);
    };

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modal}>
                <h2 className={styles.modalTitle}>Add Company</h2>

                <label className={styles.fileLabel}>Company Name</label>
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />

                <div className={styles.modalActions}>
                    <button className={styles.secondaryBtn} onClick={onClose}>
                        Cancel
                    </button>
                    <button className={styles.primaryBtn} onClick={handleSubmit}>
                        Add Company
                    </button>
                </div>
            </div>
        </div>
    );
}
