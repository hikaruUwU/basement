export const $copy = (text: string | number) => {
    const content = String(text);

    const fallbackCopy = (resolve: () => void, reject: (e: any) => void) => {
        const textArea = document.createElement("textarea");
        textArea.value = content;
        Object.assign(textArea.style, {
            position: "fixed",
            left: "-9999px",
            top: "0"
        });
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();
        try {
            document.execCommand('copy') ? resolve() : reject('ExecCommand failed');
        } catch (err) {
            reject(err);
        } finally {
            document.body.removeChild(textArea);
        }
    };

    return new Promise<void>((resolve, reject) => {
        if (navigator.clipboard && window.isSecureContext) {
            navigator.clipboard.writeText(content)
                .then(resolve)
                .catch(() => fallbackCopy(resolve, reject));
        } else {
            fallbackCopy(resolve, reject);
        }
    });
};