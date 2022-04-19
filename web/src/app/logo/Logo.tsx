import logoImg from '../../images/logo/logo.png';

const Logo = (props: { onClick: () => void }) => {
    return (
        <img
            onClick={() => props.onClick()}
            width='38px'
            height='38px'
            src={logoImg}
            alt={'logo'}
            style={{cursor: 'pointer'}}
        />
    );
};

export default Logo;
