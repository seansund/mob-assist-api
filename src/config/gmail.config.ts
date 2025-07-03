import {EmptyConfig} from './empty.config';

export const GMAIL_CONFIG = 'config.GmailConfig';

export interface GmailConfig {
  clientId: string;
  privateKey: string;
  smtpHost: string;
  smtpPort: number;
  fromEmail: string;
  subjectPrefix: string;
  channel: string;
  valid: boolean;
}

const buildGmailConfig = (): GmailConfig | EmptyConfig => {
  const clientId = process.env.GMAIL_CLIENT_ID;
  const privateKey = process.env.GMAIL_PRIVATE_KEY;
  const smtpHost = process.env.GMAIL_SMTP_HOST;
  const smtpPort = parseInt(process.env.GMAIL_SMTP_PORT ?? '465');
  const fromEmail = process.env.GMAIL_FROM_EMAIL;
  const subjectPrefix = process.env.GMAIL_SUBJECT_PREFIX ?? 'FBG: ';

  if (!clientId || !privateKey || !smtpHost || !fromEmail) {
    return {
      valid: false,
    }
  }

  return {
    clientId,
    privateKey,
    smtpHost,
    smtpPort,
    fromEmail,
    subjectPrefix,
    channel: 'email',
    valid: true,
  }
}

export const gmailConfig: GmailConfig | EmptyConfig = buildGmailConfig();
