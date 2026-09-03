const webSocketProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';

export const environment = {
  webSocketBaseUrl: `${webSocketProtocol}//${window.location.host}`,
};
