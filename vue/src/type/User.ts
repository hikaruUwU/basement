interface base {
    id: string;
    username: string;
    password: string;
    roles: string[];
    enabled: boolean;
}


export type User = Partial<base>